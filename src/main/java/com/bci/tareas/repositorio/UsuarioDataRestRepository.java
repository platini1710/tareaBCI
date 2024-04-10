package com.bci.tareas.repositorio;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bci.tareas.model.Usuario;




@Repository 
public interface UsuarioDataRestRepository extends CrudRepository<Usuario, String> {
	public Optional<Usuario> findById(String id);
	public List< Usuario> findAll();


	  public  Usuario save(Usuario usuario);
	  public void deleteById(String id);
	  
	  @Query("from Usuario u where u.email=:email")
	  public List<Usuario> findEmail(@Param("email") String email);
}