package com.RWI.Nidhi.user.serviceInterface;

import com.RWI.Nidhi.dto.SaveIdentityDocsDTO;
import org.springframework.web.multipart.MultipartFile;

public interface IdentityDocsService {

    String uploadFile(SaveIdentityDocsDTO saveIdentityDocsDTO);

    String getDownloadUrlForFront(Integer id);

    String getDownloadUrlForBack(Integer id);

    String getProfilePhoto(Integer id);

    String updateProfilePhoto(Integer id, MultipartFile profilePhoto);


}



