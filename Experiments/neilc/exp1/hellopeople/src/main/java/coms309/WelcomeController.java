
package coms309;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;


@RestController
class WelcomeController {

    PeopleController pc = new PeopleController();

    @GetMapping("/")
    public String welcome() {
        return "Hello and welcome to COMS 309";
    }


    @GetMapping("/{name}")
    public String welcome(@PathVariable String name) {
        return "Hello and welcome to COMS 309: " + pc.peopleList.get(name);
    }

    @GetMapping("/people")
    public @ResponseBody HashMap<String,Person> getAllPersons() {
        return pc.peopleList;
    }


    @PostMapping("/people")
    public @ResponseBody String createPerson(@RequestBody Person person){
        System.out.println(person.getFirstName());
        pc.peopleList.put(person.getFirstName(), person);
        return "new person " + person.getFirstName() + " saved";
    }

    @GetMapping("/people/{firstName}")
    public @ResponseBody Person getPerson(@PathVariable String firstName){
        Person p = pc.peopleList.get(firstName);
        return p;
    }

    @PutMapping("/people/{firstName}")
    public @ResponseBody String updatePerson(@PathVariable String firstName, @RequestBody Person p) {
        String data = "changing " + pc.peopleList.get(firstName) +" to ";
        pc.peopleList.replace(firstName, p);
        return data + pc.peopleList.get(firstName);
    }

    @DeleteMapping("/people/{firstName}")
    public @ResponseBody HashMap<String, Person> deletePerson(@PathVariable String firstName) {
        pc.peopleList.remove(firstName);
        return pc.peopleList;
    }


}
