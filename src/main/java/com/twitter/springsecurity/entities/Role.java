package com.twitter.springsecurity.entities;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "tb_roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="role_id")
    private Long roleId;

    private String name;

    public Role(Long id) {
    }

    public Role() {
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public enum Values {

        ADMIN(1L),
        BASIC(2L),
        BLOCKED(3L);

        private Long id;

        Values(Long id) {
            this.id = id;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Role toRoles(){
            return new Role(id);
        }
    }
}
