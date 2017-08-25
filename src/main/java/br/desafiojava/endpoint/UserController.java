package br.desafiojava.endpoint;

import br.desafiojava.entity.Phone;
import br.desafiojava.entity.User;
import br.desafiojava.utils.HibernateUtils;
import br.desafiojava.utils.Password;
import java.util.ArrayList;
import java.util.Map;
import org.hibernate.Session;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import java.security.Key;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Date;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
public class UserController {

    @RequestMapping(value = "/user", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> user(@RequestBody Map<String, Object> user) {
        Session session = HibernateUtils.getSessionFactory().openSession();
        Criteria cri = session.createCriteria(User.class);
        cri.add(Restrictions.eq("email", (String) user.get("email")));
        cri.setMaxResults(1);
        User u = (User) cri.uniqueResult();
        
        if(u != null ){
            return new ResponseEntity<String>("{ \"mensagem\": \"E-mail já existente\"}", HttpStatus.UNAUTHORIZED);
        }
        
        Key key = MacProvider.generateKey();

        String token = Jwts.builder()
          .setSubject((String) user.get("name"))
          .signWith(SignatureAlgorithm.HS512, key)
          .compact();
        
        u = new User();
        u.setName((String) user.get("name"));
        u.setEmail((String) user.get("email"));
        u.setPassword(Password.hashPassword((String) user.get("password")));
        u.setPhones(new ArrayList<>());
        u.setToken(token);
        u.setCreated(new Date());
        u.setModified(new Date());
        u.setLast_login(new Date());
        
        for(Map<String, Object> obj : (ArrayList<Map<String, Object>>) user.get("phones")){
            Phone p = new Phone();
            p.setDdd((String) obj.get("ddd"));
            p.setNumber((String) obj.get("number"));
            p.setUser(u);
            
            u.getPhones().add(p);
        }
                
        session.beginTransaction();
        session.save(u);
        for(Phone p : u.getPhones()){
            session.save(p);
        }
        
        session.getTransaction().commit();
        
        User aux = new User();
        aux.setId(u.getId());
        aux.setCreated(u.getCreated());
        aux.setModified(u.getModified());
        aux.setLast_login(u.getLast_login());
        aux.setToken(u.getToken());
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(aux, User.class);
        
        return new ResponseEntity<String>(json, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> login(@RequestBody Map<String, Object> user) {
        Session session = HibernateUtils.getSessionFactory().openSession();
        Criteria cri = session.createCriteria(User.class);
        cri.add(Restrictions.eq("email", (String) user.get("email")));
        cri.setMaxResults(1);
        User u = (User) cri.uniqueResult();
        
        if(u == null || !Password.checkPassword((String) user.get("password"), u.getPassword())){
            return new ResponseEntity<String>("{ \"mensagem\": \"Usuário e/ou senha inválidos\"}", HttpStatus.UNAUTHORIZED);
        }
        
        session.getTransaction().begin();
        u.setLast_login(new Date());
        session.save(u);
        session.getTransaction().commit();
        
        User aux = new User();
        aux.setId(u.getId());
        aux.setCreated(u.getCreated());
        aux.setModified(u.getModified());
        aux.setLast_login(u.getLast_login());
        aux.setToken(u.getToken());
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(aux, User.class);
        
        return new ResponseEntity<String>(json, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/consume", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> consume(@RequestBody Map<String, Object> user) {
        Integer idAux = (Integer) user.get("id");
        
        if(idAux == null){
            return new ResponseEntity<String>("{ \"mensagem\": \"Não autorizado\"}", HttpStatus.UNAUTHORIZED);
        }
        
        Long id = new Long(idAux);
        String token = (String) user.get("token");
        
        if(token == null || token.isEmpty()){
            return new ResponseEntity<String>("{ \"mensagem\": \"Não autorizado\"}", HttpStatus.UNAUTHORIZED);
        }
        
        Session session = HibernateUtils.getSessionFactory().openSession();
        Criteria cri = session.createCriteria(User.class);
        cri.add(Restrictions.eq("id", id));
        cri.setMaxResults(1);
        User u = (User) cri.uniqueResult();
        
        if(u == null){
            return new ResponseEntity<String>("{ \"mensagem\": \"Não autorizado\"}", HttpStatus.UNAUTHORIZED);
        } else if(!u.getToken().equals(token)){
            return new ResponseEntity<String>("{ \"mensagem\": \"Não autorizado\"}", HttpStatus.UNAUTHORIZED);
        }
        
        long diff = new Date().getTime() - u.getLast_login().getTime();
        long diffMinutes = diff / (60 * 1000) % 60; 
        
        if(diffMinutes > 30){
            return new ResponseEntity<String>("{ \"mensagem\": \"Sessão inválida\"}", HttpStatus.UNAUTHORIZED);
        }
        
        //Evita loop na criação do JSON
        for(Phone p : u.getPhones()){
            p.setUser(null);
            p.setId(null);
        }
        //Remove informações redundantes ou restrita
        u.setPassword(null);
        u.setToken(null);
        
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(u, User.class);
        
        return new ResponseEntity<String>(json, HttpStatus.OK);
    }
    
    
}
