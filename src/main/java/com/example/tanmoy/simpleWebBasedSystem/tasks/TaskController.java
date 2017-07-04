package com.example.tanmoy.simpleWebBasedSystem.tasks;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class TaskController {
	@Autowired
	TaskService taskService;

	@GetMapping(value = "/")
	public String taskList(Model model) {
		model.addAttribute("taskList", taskService.findAllTask());
		return "";
	}

	@GetMapping(value = "/create_task")
	public String createTask(Model model) {
		model.addAttribute("taskEntity", new TaskEntity());
		model.addAttribute("title", "Create Task");
		return "";
	}

	@PostMapping(value = "/create_task")
	public String createTask(Model model, @ModelAttribute TaskEntity taskEntity, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("taskEntity", taskEntity);
			model.addAttribute("title", "Create Task");
			return "";
		} else {
			taskEntity.setDateCreated(new Date());
			taskService.saveTask(taskEntity);
			return "redirect:/";
		}

	}

	@GetMapping(value = "/update_task/{id}")
	public String updateTask(Model model, @PathVariable int id) {
		model.addAttribute("taskEntity", taskService.getTaskById(id));
		model.addAttribute("title", "Update Task");
		return "";
	}

	@PostMapping(value = "/update_task/{id}")
	public String updateTask(Model model, @PathVariable int id, @ModelAttribute TaskEntity taskEntity,
			BindingResult bindingResult) {
		if (id == taskEntity.getId()) {
			TaskEntity taskEntityBeforeUpdate=taskService.getTaskById(taskEntity.getId());
			if (bindingResult.hasErrors()) {
				model.addAttribute("taskEntity", taskEntityBeforeUpdate);
				model.addAttribute("title", "Update Task");
				return "";
			} else {
				taskEntity.setDateCreated(taskEntityBeforeUpdate.getDateCreated());
				taskEntity.setDateUpdated(new Date());
				taskService.saveTask(taskEntity);
				return "redirect:/";
			}
		} else {
			bindingResult.reject("500", "Bad Request");
			model.addAttribute("taskEntity", taskService.getTaskById(id));
			model.addAttribute("title", "Update Task");
			return "";
		}
	}
	
	@GetMapping(value="/delete/{id}")
	public String deleteTask(@PathVariable int id, Model model){
		taskService.deleteTask(id);
		model.addAttribute("taskList", taskService.findAllTask());
		return "";
	}

}
