// Selectors
const cartButtonElementList = document.getElementsByClassName('store-item');
const cartDeleteItemElementList = document.getElementsByClassName('cart-delete');
const cartTotalSumElement = document.getElementsByClassName('cart-total-price')[0];
const cartQuantityElement = document.getElementsByClassName('cart-quantity');
const cartQuantityPlusElement = document.getElementsByClassName('cart-quantity-plus');
const cartQuantityMinusElement = document.getElementsByClassName('cart-quantity-minus');


// Event Listeners
for(let i = 0; i < cartButtonElementList.length; i++) {
    const cartBtn = cartButtonElementList[i];
    cartBtn.addEventListener('click', event => {
        let cartItem = {};
        const cardElement = event.target.parentElement.parentElement.parentElement;
        const imageElement = cardElement.getElementsByClassName('card-img-top')[0];
        const titleElement = cardElement.getElementsByClassName('book-title')[0];
        const authorElement = cardElement.getElementsByClassName('book-author')[0];
        const priceElement = cardElement.getElementsByClassName('book-price')[0];

        cartItem.bookId = cardElement.getAttribute('data-id');
        cartItem.imageUrl = imageElement.getAttribute('src');
        cartItem.title = titleElement.innerText;
        cartItem.author = authorElement.innerText;
        cartItem.price = parseFloat(priceElement.innerText);
        cartItem.quantity = 1;

        // async call to server to update cart.
        addBookToCart(cartItem);
    });
}

for(let i = 0; i < cartDeleteItemElementList.length; i++) {
    const deleteBtn = cartDeleteItemElementList[i];
    deleteBtn.addEventListener('click', event => {
        const cartItemElement = event.target.parentElement.parentElement;
        const bookTotalPriceElement = cartItemElement.getElementsByClassName('book-total-price')[0];

        const bookId = parseInt(cartItemElement.getAttribute('data-id'));
        const bookTotalPrice = parseFloat(bookTotalPriceElement.innerText);
        const cartTotalPrice = getCartTotalSum();
        cartItemElement.remove();
        // call server to remove book item from session.
        removeBookFromCart(bookId);
        updateCartTotalSum(cartTotalPrice - bookTotalPrice);
    });
}

for(let i = 0; i < cartQuantityPlusElement.length; i++) {
    const plusBtn = cartQuantityPlusElement[i];
    plusBtn.addEventListener('click', event => {
        handlePlusMinusBookQuantity(event, '+');
    });
}

for(let i = 0; i < cartQuantityMinusElement.length; i++) {
    const minusBtn = cartQuantityMinusElement[i];
    minusBtn.addEventListener('click', event => {
       handlePlusMinusBookQuantity(event, '-');
    });
}


// Utility Functions
function handleSelectCategory(e) {
    const id = e.target.getAttribute("name");
    let url = "http://localhost:8080/";

    if (id > 0)
        url += "books/" + id;
    window.location.href = url;
}

function handlePlusMinusBookQuantity(event, operation) {
    let cartItem = {};
    let newQuantity = 0;
    const cartItemElement = event.target.parentElement.parentElement.parentElement.parentElement;
    const cartQuantityElement = cartItemElement.getElementsByClassName('cart-quantity')[0];
    const bookTotalPriceElement = cartItemElement.getElementsByClassName('book-total-price')[0];
    const bookPriceElement = cartItemElement.getElementsByClassName('book-price')[0];

    const currentQuantity = parseInt(cartQuantityElement.innerText);
    const bookPrice = parseFloat(bookPriceElement.innerText);
    const cartTotalSum = getCartTotalSum();

    if(operation === '-') {
        newQuantity = (currentQuantity - 1) <= 0 ? 1 : (currentQuantity - 1);
        if ((currentQuantity - 1) > 0)
            updateCartTotalSum(cartTotalSum - bookPrice);
    }
    else {
        let instock = 5;
        newQuantity = (currentQuantity + 1) > instock ? currentQuantity : currentQuantity + 1 ;
        if ( (currentQuantity + 1)  <= instock)
             updateCartTotalSum(cartTotalSum + bookPrice);
    }

    cartItem.quantity = newQuantity;
    cartItem.bookId = cartItemElement.getAttribute('data-id');

    updateCartQuantity(cartItem);

    const newTotalPrice = bookPrice * newQuantity;
    bookTotalPriceElement.innerText = newTotalPrice;
    cartQuantityElement.innerText = newQuantity;
}

function getCartTotalSum() {
    return parseFloat(cartTotalSumElement.innerText.replace('$ ', ''));
}

function updateCartTotalSum(value) {
    cartTotalSumElement.innerText=`$ ${value}`;
}


// Calling Backend Services.
async function addBookToCart(cartItem) {
    try {
        const response = await axios.post('/cart/add', cartItem);
        alert('Book successfully added to cart.');
    } catch (error) {
        alert('Cannot add book to cart.');
    }
}

async function updateCartQuantity(cartItem) {
    try {
        const response = await axios.put('/cart/update', cartItem);
    } catch (error) {
        console.log(error);
    }
}

async function removeBookFromCart(bookId) {
    try {
        const response = await axios.delete('/cart/remove/' + bookId);
        console.log(response);
    }catch (error) {
        alert('Cannot remove book from cart.');
        window.location.href = "http://localhost:8080/cart";
    }
}