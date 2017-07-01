package com.example.tanmoy.simpleWebBasedSystem;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class TaskDao {
	private Session session;
	private Session getSession(){
		return this.session;
	}
	
	public void saveOrUpdate(TaskEntity taskEntity){
		getSession().saveOrUpdate(taskEntity);
	}
	
	public void delete(TaskEntity taskEntity){
		getSession().delete(taskEntity);
	}
	
	@SuppressWarnings("unchecked")
	public List<TaskEntity> getAll(){
		return getSession().createCriteria(TaskEntity.class).list();
	}
	
	public TaskEntity getById(int id){
		Criteria criteria=getSession().createCriteria(TaskEntity.class);
		criteria.add(Restrictions.idEq(id));
		return (TaskEntity) criteria.uniqueResult();
	}

}
