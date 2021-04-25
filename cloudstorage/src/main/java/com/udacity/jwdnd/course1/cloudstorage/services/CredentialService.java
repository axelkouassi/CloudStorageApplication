package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CredentialService {
    private CredentialMapper credentialMapper;
    private EncryptionService encryptionService;

    public CredentialService(CredentialMapper credentialMapper, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
    }

    public String encryptPassword(Credentials credential) {
        return this.encryptionService.encryptValue(credential.getPassword(), credential.getKey());
    }

    public int  addCredential(Credentials credential) {
        String key = this.encryptionService.generateKey();
        credential.setKey(key);
        credential.setPassword(this.encryptPassword(credential));
        return this.credentialMapper.insert(credential);
    }

    public List<Credentials> getCredentialsForUser(Integer userId) {
        return this.credentialMapper.getCredentialsForUser(userId);
    }

    public Credentials getCredentialById(Integer credentialId) {
        return this.credentialMapper.getCredentialById(credentialId);
    }

    public void deleteCredential(Integer credentialId, Integer userId) {
        this.credentialMapper.delete(credentialId);
    }

    public void editCredential(Credentials credential) {
        Credentials cred = this.credentialMapper.retrieveKeyByCredentialId(credential.getCredentialId());
        credential.setKey(cred.getKey());
        String encodedPassword = this.encryptionService.encryptValue(credential.getPassword(), credential.getKey());
        credential.setPassword(encodedPassword);
        this.credentialMapper.update(credential);
    }
}
