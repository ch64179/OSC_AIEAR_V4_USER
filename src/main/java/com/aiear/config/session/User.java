/**
 * 
 */
package com.aiear.config.session;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_user_info")
public class User {
    @Id
    private String userId;
    private String userNm;
    private String userPwd;
    private String userType;
}