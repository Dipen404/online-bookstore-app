package com.dipen.bookstore.utility;

import com.dipen.bookstore.books.entity.Category;
import com.dipen.bookstore.books.repository.CategoryRepository;
import com.dipen.bookstore.user.entity.Role;
import com.dipen.bookstore.user.entity.User;
import com.dipen.bookstore.user.entity.UserRole;
import com.dipen.bookstore.user.service.RoleService;
import com.dipen.bookstore.user.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Component
@ConditionalOnProperty(value = "runner.enabled", matchIfMissing = true, havingValue = "true")
public class AddStaticValuesRunner implements CommandLineRunner {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        Category fantasy = new Category("Arts & Literature");
        Category adventure = new Category("Adventure");
        Category romance = new Category("Romance");
        Category mystery = new Category("Mystery");
        Category horror = new Category("Horror");
        Category thriller = new Category("Thriller");

       // categoryRepository.saveAll(List.of(fantasy, horror, adventure, romance, mystery, thriller));
        
        List<Category> list = new ArrayList<Category>();
        list.add(fantasy);
        list.add(adventure);
        list.add(romance);
        list.add(mystery);
        list.add(thriller);
        list.add(horror);
        
        categoryRepository.saveAll(list);


        User user = new User("admin@bookstore.com", passwordEncoder.encode("password"), "ADMIN", "ADMIN");
        Role role = roleService.findOrCreateRole("ROLE_ADMIN");
        //Set<UserRole> userRoles = Set.of(new UserRole(user, role));
        Set<UserRole> userRoles = new HashSet<UserRole>();
        userRoles.add(new UserRole(user, role));
        userService.createUser(user, userRoles);
    }
}
