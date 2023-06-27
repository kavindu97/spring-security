package security.example.security.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Data
@NoArgsConstructor
@Table(name = "Roles")
public class Role {
    @PrePersist
    protected void onCreate(){
        this.created_At=new Date(System.currentTimeMillis());
    }

    @PreUpdate
    protected void onUpdate(){
        this.updated_At=new Date(System.currentTimeMillis());
    }



    @Id
    @SequenceGenerator(name = "role_sequence",
        sequenceName = "role_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.AUTO,
            generator = "role_sequence"
    )
    private Long id;
    private String name;
    private String description;
    private Date created_At;
    private Date updated_At;
    @ManyToMany(mappedBy = "roles")
    @Fetch(value = FetchMode.SELECT)
    @JsonIgnore
    private Set<User> user=new HashSet<>();
    public Role(Long id,String name,String description){
        this.id=id;
        this.name=name;
        this.description=description;
    }
    public Role(String name){
        this.name=name;
    }

}
