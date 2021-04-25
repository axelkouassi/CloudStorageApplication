package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialMapper {

    @Insert("INSERT INTO CREDENTIALS (url, username, key, password, userid) " +
            "VALUES(#{url}, #{username}, #{key}, #{password}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialId")
    int insert(Credentials credential);

    @Select("SELECT * FROM CREDENTIALS WHERE userid = #{userId}")
    List<Credentials> getCredentialsForUser(Integer userId);

    @Select("SELECT * FROM CREDENTIALS WHERE credentialid = #{credentialId}")
    Credentials getCredentialById(Integer credentialId);

    @Select("SELECT * FROM CREDENTIALS WHERE url = #{url} AND userid = #{userId}")
    Credentials getCredentials(String url, Integer userId);


    @Select("SELECT * FROM CREDENTIALS WHERE key = #{key} AND credentialid = #{credentialid}")
    Credentials retrieveKeyByCredentialId(Integer credentialId);

    @Update("UPDATE CREDENTIALS SET url = #{url}, username = #{username}, password = #{password} " +
            "userid = #{userId}, key = #{key} WHERE credentialid = #{credentialId}")
    int update(Credentials credential);

    @Delete("DELETE FROM CREDENTIALS WHERE credentialid = #{credentialId}")
    void delete(Integer credentialid);


}


