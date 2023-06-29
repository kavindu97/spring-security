package security.example.security.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Getter
@Setter
@Data
@NoArgsConstructor
@Table(name = "Users")
public class User implements UserDetails {
    @PrePersist
    protected void onCreate(){
        this.createdAt=new Date(System.currentTimeMillis());
    }

    @PreUpdate
    protected void onUpdate(){
        this.updatedAt=new Date(System.currentTimeMillis());
    }
    @Id
    private String userId;
    private String user_name;// there is a inbuilt variable userName in UserDetails class
    private String email;
    private String password;
    private String mobileNumber;
    @ManyToMany
    @JoinTable(name = "user_role",
    joinColumns = @JoinColumn(name="User_id"),
            inverseJoinColumns = @JoinColumn(name = "Role_id")
    )
    private Set<Role> roles = new HashSet<>();

    private Date createdAt;
    private Date updatedAt;




    public User(String mobileNumber,String userName,String email,String password,Set<Role> roles){
        this.userId=email;
        this.mobileNumber=mobileNumber;
        this.user_name=userName;
        this.email=email;
        this.password=password;
        this.roles=roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<SimpleGrantedAuthority> authorities=new ArrayList<>();
        roles.forEach(i->authorities.add(new SimpleGrantedAuthority(i.getName())));
        return List.of(new SimpleGrantedAuthority(authorities.toString()));
    }

    @Override
    public String getUsername() {
        return email;
    }
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
