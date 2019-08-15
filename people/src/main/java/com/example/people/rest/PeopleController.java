package com.example.people.rest;

import com.example.people.service.PeopleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author anonymity
 * @create 2019-08-13 17:52
 **/
@RestController
@RequestMapping("/people")
public class PeopleController {

    @Resource
    private PeopleService peopleService;

    @GetMapping("/deposit/{amount}")
    public String deposit(@PathVariable @NotNull @Min(0) int amount) {
        return peopleService.deposit(amount);
    }

}
