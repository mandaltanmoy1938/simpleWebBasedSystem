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

@Controller
public class TaskController {
	@Autowired
	TaskService taskService;

	@GetMapping(value = "/")
	public String taskList(Model model) {
		model.addAttribute("taskList", taskService.findAllTask());
		return "/tasks/task_list";
	}

	@GetMapping(value = "/task/create")
	public String createTask(Model model) {
		model.addAttribute("taskEntity", new TaskEntity());
		model.addAttribute("title", "Create Task");
		model.addAttribute("url", "create");
		return "/tasks/save_task";
	}

	@PostMapping(value = "/task/create")
	public String createTask(Model model, @Valid @ModelAttribute TaskEntity taskEntity, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("taskEntity", taskEntity);
			model.addAttribute("title", "Create Task");
			return "/tasks/save_task";
		} else {
			taskEntity.setDateCreated(new Date());
			taskService.saveTask(taskEntity);
			return "redirect:/";
		}

	}

	@GetMapping(value = "/task/update/{id}")
	public String updateTask(Model model, @PathVariable int id) {
		model.addAttribute("taskEntity", taskService.getTaskById(id));
		model.addAttribute("title", "Update Task");
		model.addAttribute("url", "update");
		return "/tasks/save_task";
	}

	@PostMapping(value = "/task/update/{id}")

	public String updateTask(Model model, @PathVariable int id, @Valid @ModelAttribute TaskEntity taskEntity,
			BindingResult bindingResult, HttpServletResponse res) {
		if (id == taskEntity.getId()) {
			if (bindingResult.hasErrors()) {
				model.addAttribute("taskEntity", taskEntity);
				model.addAttribute("title", "Update Task");
				model.addAttribute("url", "update");
				return "/tasks/save_task";
			} else {
				TaskEntity taskEntityBeforeUpdate = taskService.getTaskById(taskEntity.getId());
				taskEntity.setDateCreated(taskEntityBeforeUpdate.getDateCreated());
				taskEntity.setDateUpdated(new Date());
				taskService.saveTask(taskEntity);
				return "redirect:/";
			}
		} else {
			bindingResult.reject("400", "Bad Request");
			model.addAttribute("taskEntity", taskService.getTaskById(id));
			model.addAttribute("title", "Update Task");
			model.addAttribute("url", "update");
			res.setStatus(HttpStatus.BAD_REQUEST.value());
			return "/tasks/save_task";
		}
	}

	@GetMapping(value = "/task/delete/{id}")
	public String deleteTask(@PathVariable int id, Model model) {
		taskService.deleteTask(id);
		return "redirect:/";
	}

}
