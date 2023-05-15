package com.example.restapi2.controller;
import com.example.restapi2.Models.Photo;
import com.example.restapi2.Models.User;
import com.example.restapi2.Models.Emlak;

import com.example.restapi2.Repo.PhotoRepo;
import com.example.restapi2.Repo.UserRepo;
import com.example.restapi2.Repo.EmlakRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@RestController
public class ApiController {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PhotoRepo photoRepo;
    @Autowired
    private EmlakRepo emlakRepo;

    @GetMapping(value = "/")
    public String getPage(){
        return "Welcome";
    }

    @GetMapping(value="/users")
    public List<User> getUsers(){
        return userRepo.findAll();
    }

    @GetMapping(value="/users/{id}")
    public User getUser(@PathVariable long id){
        return userRepo.findById(id).orElse(null);
    }
    @PostMapping(value = "/save")
    public String saveUser(@RequestBody User user){
        userRepo.save(user);
        return String.valueOf(user.getId());
    }
    @CrossOrigin
    @GetMapping(value="/evler")
    public List<Emlak> getEvler(){
        return emlakRepo.findAll();
    }

    @CrossOrigin
    @PostMapping(value="/filtre")
    public List<Emlak> getKiralik(@RequestBody Emlak filtre){
        String isinma = filtre.getIsinma();
        String kiralik = filtre.getKiralik();
        String tur = filtre.getTur();
        return emlakRepo.filtreleEmlak(isinma,kiralik,tur);
    }
    @CrossOrigin
    @GetMapping(value="/evler/{id}")
    public Emlak getEv(@PathVariable long id){
        return emlakRepo.findById(id).orElse(null);
    }
    @CrossOrigin
    @PostMapping(value = "/kaydet")
    public ResponseEntity<?> emlakOlustur(@RequestParam("mkare") String mkare,
                                          @RequestParam("osayi") String osayi,
                                          @RequestParam("bkati") String bkati,
                                          @RequestParam("bbkat") String bbkat,
                                          @RequestParam("isinma") String isinma,
                                          @RequestParam("ucret") String ucret,
                                          @RequestParam("tur") String tur,
                                          @RequestParam("kiralik") String kiralik,
                                          @RequestParam("foto") MultipartFile foto,
                                          @RequestParam("tam_adres") String tam_adres,
                                          @RequestParam("sehir") String sehir,
                                          @RequestParam("sokak") String sokak,
                                          @RequestParam("zip") String zip) {
        try {
            String contentType = foto.getContentType();
            if (!contentType.equals("image/jpeg") && !contentType.equals("image/png") && !contentType.equals("image/jpg")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Desteklenmeyen dosya türü.");
            }

            // Yüklenen dosyayı kaydetmek için gereken kodu yazın
            String fileName = StringUtils.cleanPath(foto.getOriginalFilename());
            Path path = Paths.get("C:\\safesite_documents\\" + fileName);
            Files.copy(foto.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            Emlak emlak = new Emlak(mkare, osayi, bkati, bbkat, isinma, ucret, tur, kiralik, "C:\\\safesite_documents\\" +fileName, tam_adres, sehir, sokak, zip);
            emlakRepo.save(emlak);
            return ResponseEntity.ok().body("Başarılı");
        }catch (IOException e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }


    }
    @PostMapping(value = "/saved")
    public ResponseEntity<?> createUser(@RequestParam("firstName") String firstName,
                                        @RequestParam("lastName") String lastName,
                                        @RequestParam("image") MultipartFile image) {
        try {
            Photo photo = new Photo(firstName, lastName, image.getBytes());
            photoRepo.save(photo);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PostMapping(value = "/update/{id}")
    public String updateUser(@PathVariable long id, @RequestBody User user){
        User updatedUser = userRepo.findById(id).get();
        updatedUser.setUsername(user.getUsername());
        updatedUser.setLastname(user.getLastname());
        updatedUser.setEmail(user.getEmail());
        userRepo.save(updatedUser);
        return "Updated ...";
    }

    @DeleteMapping(value = "/delete/{id}")
    public String deleteUser(@PathVariable long id){
        User deleteUser = userRepo.findById(id).get();
        userRepo.delete(deleteUser);
        return "Delete user with the id:"+id;
    }
}
