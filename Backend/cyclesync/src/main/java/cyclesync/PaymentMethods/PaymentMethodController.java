package cyclesync.PaymentMethods;

import cyclesync.Users.User;
import cyclesync.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController

public class PaymentMethodController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PaymentMethodRepository paymentMethodRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/payment_methods/{id}")
    List<PaymentMethod> getPaymentMethodsById(@PathVariable int id){

        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return Collections.emptyList();
        }
        return user.getPaymentMethods();
    }

    @PostMapping(path="/payment_methods/{id}")
    String addPaymentMethod(@PathVariable int id, @RequestBody PaymentMethod request) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return failure;
        }

        paymentMethodRepository.save(request);

        user.addPaymentMethod(request);
        userRepository.save(user);
        return success;
    }

    @DeleteMapping(path="/payment_methods/{user_id}/{pm_id}")
    String removePaymentMethod(@PathVariable int user_id, @PathVariable int pm_id) {
        User user = userRepository.findById(user_id).orElse(null);
        if (user == null) {
            return failure;
        }

        List<PaymentMethod> paymentMethods = user.getPaymentMethods();
        PaymentMethod paymentMethodToRemove = null;
        for (PaymentMethod pm : paymentMethods) {
            if (pm.getId() == pm_id) {
                paymentMethodToRemove = pm;
                break;
            }
        }

        if (paymentMethodToRemove != null) {
            paymentMethods.remove(paymentMethodToRemove);
            user.setPaymentMethods(paymentMethods); // Assume User has setPaymentMethods() method
            userRepository.save(user);
            return success;
        } else {
            return failure; // PaymentMethod not found for that id
        }
    }

}
