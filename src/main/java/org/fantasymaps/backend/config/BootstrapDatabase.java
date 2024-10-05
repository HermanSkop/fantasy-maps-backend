package org.fantasymaps.backend.config;

import org.fantasymaps.backend.model.Category;
import org.fantasymaps.backend.model.Subscription;
import org.fantasymaps.backend.model.Tag;
import org.fantasymaps.backend.model.product.Bundle;
import org.fantasymaps.backend.model.product.Map;
import org.fantasymaps.backend.model.user.Creator;
import org.fantasymaps.backend.model.user.Customer;
import org.fantasymaps.backend.repositories.CategoryRepository;
import org.fantasymaps.backend.repositories.SubscriptionRepository;
import org.fantasymaps.backend.repositories.TagRepository;
import org.fantasymaps.backend.repositories.product.MapRepository;
import org.fantasymaps.backend.repositories.product.ProductRepository;
import org.fantasymaps.backend.repositories.user.CreatorRepository;
import org.fantasymaps.backend.repositories.user.CustomerRepository;
import org.fantasymaps.backend.repositories.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Component
public class BootstrapDatabase implements CommandLineRunner {
    private final CustomerRepository customerRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final ProductRepository productRepository;
    private final TagRepository tagRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final CreatorRepository creatorRepository;
    private final MapRepository mapRepository;

    @Autowired
    public BootstrapDatabase(CustomerRepository customerRepository, SubscriptionRepository subscriptionRepository, ProductRepository productRepository, TagRepository tagRepository, CategoryRepository categoryRepository, UserRepository userRepository, CreatorRepository creatorRepository, MapRepository mapRepository) {
        this.customerRepository = customerRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.productRepository = productRepository;
        this.tagRepository = tagRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.creatorRepository = creatorRepository;
        this.mapRepository = mapRepository;
    }

    @Override
    public void run(String... args) {
        if (customerRepository.count() != 0) return;

        createCreators();
        createSubscriptions(creatorRepository.findById(1).orElseThrow());
        createCategories();
        createTags();
        createMaps(creatorRepository.findById(1).orElseThrow(), new HashSet<>(categoryRepository.findAll()));
        createBundles(mapRepository.findByCreator_Id(1));
        createCustomers();
    }

    private void createCreators() {
        Set<Creator> creators = Set.of(Creator.builder().username("creator1").email("creator1@example.com").password("Password1").date(LocalDate.now().minusDays(10)).avatarUrl("public/avatar1.png").description("I am a creator specializing in digital art.").subscriptions(new HashSet<>()).products(new HashSet<>()).build(), Creator.builder().username("creator2").email("creator2@example.com").password("Password2").date(LocalDate.now().minusDays(20)).avatarUrl("public/avatar2.png").description("I create 3D models for games and animations.").subscriptions(new HashSet<>()).products(new HashSet<>()).build(), Creator.builder().username("creator3").email("creator3@example.com").password("Password3").date(LocalDate.now().minusDays(30)).avatarUrl("public/avatar3.png").description("My focus is on educational video content.").subscriptions(new HashSet<>()).products(new HashSet<>()).build());
        userRepository.saveAll(creators);
    }

    private void createSubscriptions(Creator creator) {
        Set<Subscription> subscriptions = Set.of(Subscription.builder().name("Basic").price(1.0).description("Basic subscription with access to standard content.").products(new HashSet<>()).creator(creator).subscribedCustomers(new HashSet<>()).date(LocalDate.now().minusDays(5)).build(), Subscription.builder().name("Premium").price(5.0).description("Premium subscription with exclusive content and early access.").products(new HashSet<>()).creator(creator).subscribedCustomers(new HashSet<>()).date(LocalDate.now().minusDays(10)).build(), Subscription.builder().name("VIP").price(10.0).description("VIP subscription with direct interaction with the creator.").products(new HashSet<>()).creator(creator).subscribedCustomers(new HashSet<>()).date(LocalDate.now().minusDays(15)).build());
        subscriptionRepository.saveAll(subscriptions);
    }

    private void createBundles(Set<Map> maps) {
        Set<Bundle> bundles = Set.of(Bundle.builder().name("Bundle 1").price(1.0).dateCreated(LocalDate.now().minusDays(3)).creator(maps.iterator().next().getCreator()).maps(maps).build(), Bundle.builder().name("Bundle 2").price(2.5).dateCreated(LocalDate.now().minusDays(7)).creator(maps.iterator().next().getCreator()).maps(maps).build(), Bundle.builder().name("Bundle 3").price(4.0).dateCreated(LocalDate.now().minusDays(10)).creator(maps.iterator().next().getCreator()).maps(maps).build());
        productRepository.saveAll(bundles);
    }

    private void createMaps(Creator creator, Set<Category> categories) {
        Set<Map> maps = Set.of(Map.builder().name("Map 1").price(1.0).dateCreated(LocalDate.now().minusDays(2)).creator(creator).description("A detailed city map with landmarks.").url("public/map1.png").categories(categories).build(), Map.builder().name("Map 2").price(2.0).dateCreated(LocalDate.now().minusDays(5)).creator(creator).description("A topographical map of the mountain region.").url("public/map2.png").categories(categories).build(), Map.builder().name("Map 3").price(3.0).dateCreated(LocalDate.now().minusDays(8)).creator(creator).description("A historical map showing the evolution of a region.").url("public/map3.png").categories(categories).build());
        productRepository.saveAll(maps);
    }

    private void createCategories() {
        Set<Category> categories = Set.of(Category.builder().name("Battle Maps").build(), Category.builder().name("World Maps").build(), Category.builder().name("City Maps").build());
        categoryRepository.saveAll(categories);
    }

    private void createTags() {
        Set<Tag> tags = Set.of(Tag.builder().name("Popular").build(), Tag.builder().name("New").build(), Tag.builder().name("Public").build());
        tagRepository.saveAll(tags);
    }

    private void createCustomers() {
        Set<Customer> customers = Set.of(Customer.builder().username("customer1").email("customer1@example.com").password("Password1").date(LocalDate.now().minusDays(1)).subscriptions(new HashSet<>()).build(), Customer.builder().username("customer2").email("customer2@example.com").password("Password2").date(LocalDate.now().minusDays(10)).subscriptions(new HashSet<>()).build(), Customer.builder().username("customer3").email("customer3@example.com").password("Password3").date(LocalDate.now().minusDays(20)).subscriptions(new HashSet<>()).build(), Customer.builder().username("customer4").email("customer4@example.com").password("Password4").date(LocalDate.now().minusDays(30)).subscriptions(new HashSet<>()).build(), Customer.builder().username("customer5").email("customer5@example.com").password("Password5").date(LocalDate.now().minusDays(40)).subscriptions(new HashSet<>()).build());
        userRepository.saveAll(customers);
    }
}
