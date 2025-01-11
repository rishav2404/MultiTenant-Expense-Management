package com.grok.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.grok.entity.Expense;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Integer> {
}
