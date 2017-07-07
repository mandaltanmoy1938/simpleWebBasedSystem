package com.example.tanmoy.simpleWebBasedSystem.tasks;

import java.util.Date;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/task")
public class TaskController {
	@Autowired
	TaskService taskService;

	@GetMapping(value = "/list")
	public String taskList(Model model) {
		model.addAttribute("taskList", taskService.findAllTask());
		return "/tasks/task_list";
	}

	@GetMapping(value = "/create")
	public String createTask(Model model) {
		model.addAttribute("taskEntity", new TaskEntity());
		model.addAttribute("title", "Create Task");
		return "/tasks/save_task";
	}

	@PostMapping(value = "/save")
	public String createTask(Model model, @Valid @ModelAttribute TaskEntity taskEntity, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("taskEntity", taskEntity);
			if (taskEntity.getId() == null) {
				model.addAttribute("title", "Create Task");
			} else {
				model.addAttribute("title", "Update Task");
			}
			return "/tasks/save_task";
		} else {
			if (taskEntity.getId() == null) {
				taskEntity.setDateCreated(new Date());
			} else {
				TaskEntity taskEntityBeforeUpdate = taskService.getTaskById(taskEntity.getId());
				taskEntity.setDateCreated(taskEntityBeforeUpdate.getDateCreated());
				taskEntity.setDateUpdated(new Date());
			}
			taskService.saveTask(taskEntity);
			return "redirect:/task/list";
		}
	}

	@GetMapping(value = "/update/{id}")
	public String updateTask(Model model, @PathVariable int id) {
		model.addAttribute("taskEntity", taskService.getTaskById(id));
		model.addAttribute("title", "Update Task");
		return "/tasks/save_task";
	}

	@GetMapping(value = "/delete/{id}")
	public String deleteTask(@PathVariable int id, Model model) {
		taskService.deleteTask(id);
		return "redirect:/task/list";
	}
}
