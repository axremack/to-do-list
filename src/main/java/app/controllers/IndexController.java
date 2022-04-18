package app.controllers;

import app.entities.Category;
import app.entities.Role;
import app.entities.User;
import app.repositories.CategoryRepository;
import app.repositories.RoleRepository;
import app.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import app.entities.Task;
import app.repositories.TaskRepository;

@Controller
public class IndexController {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @GetMapping(path="/")
    public String  getIndex(Model model) {
        return "redirect:/tasks";
    }

    @GetMapping(path="/tasks")
    public String  getAllTasks(Model model) {
        model.addAttribute("title", "To-do list");
        model.addAttribute("tasks", taskRepository.findAll());
        model.addAttribute("categories", categoryRepository.findAll());
        return "allTasks";
    }

    @PostMapping(path="/tasks", params="add")
    public String createTask(@Validated Task task) {
        taskRepository.save(task); // Enregistrer la nouvelle tâche
        return "redirect:/tasks"; // Permet de passer par la fonction pour que le Model soit créé
    }

    @GetMapping(value = "/tasks/{id}")
    public String getTask(@PathVariable("id") int id, Model model) {
        model.addAttribute("title", "Tâche " + id);
        model.addAttribute("task", taskRepository.findById(Long.valueOf(id)).get());
        return "task";
    }

    @GetMapping(value = "/tasks/{id}/edit")
    public String modifyTaskForm(@PathVariable("id") int id, Model model) {
        model.addAttribute("title", "Modification de tâche " + id);
        model.addAttribute("task", taskRepository.findById(Long.valueOf(id)).get());
        model.addAttribute("categories", categoryRepository.findAll());
        return "modifyTask";
    }

    @PostMapping(path="/tasks/{id}", params="modify")
    public String modifyTask(@Validated Task task) {
        taskRepository.save(task); // Enregistrer la nouvelle tâche
        return "redirect:/tasks/{id}"; // Permet de passer par la fonction pour que le Model soit créé
    }

    @GetMapping(path="/tasks/{id}/delete")
    public String deleteTask(@PathVariable("id") int id) throws Exception {
        Task t = taskRepository.findById(Long.valueOf(id)).orElseThrow(() -> new Exception("Task not found"));
        taskRepository.delete(t);
        return "redirect:/tasks";
    }

    @GetMapping(path="/tasks/new")
    public String newTask(@ModelAttribute("task") Task task, Model model) {
        model.addAttribute("title", "Ajout de tâche");
        model.addAttribute("categories", categoryRepository.findAll());
        return "createTask";
    }

    @GetMapping(path="/login")
    public String getLogin(Model model) {
        System.out.println(userRepository.findAll());
        if(userRepository.count() == 0) {
            Role r = new Role("ADMIN");
            String mdpe = new BCryptPasswordEncoder().encode("user1");
            User u = new User("user1", "{bcrypt}"+mdpe, r);
            userRepository.save(u);
        }
        return "login";
    }

    @PostMapping(path="/login")
    public String login() {
        return "redirect:/tasks/"; // Permet de passer par la fonction pour que le Model soit créé
    }

}
