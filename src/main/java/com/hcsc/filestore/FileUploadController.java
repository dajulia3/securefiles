package hello;

import com.hcsc.filestore.SavedFileEntity;
import com.hcsc.filestore.SavedFileRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.URLConnection;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
public class FileUploadController {

    private SavedFileRepo repo;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public FileUploadController(SavedFileRepo repo) {
        this.repo = repo;
    }

    @GetMapping("/files")
    public String listUploadedFiles(Model model) throws IOException {

        model.addAttribute("files", StreamSupport.stream(repo.findAll().spliterator(), false).map(path ->
                MvcUriComponentsBuilder
                    .fromMethodName(FileUploadController.class, "serveFile", path.getName())
                    .build().toString())
            .collect(Collectors.toList()));

        return "uploadForm";
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<byte[]> serveFile(@PathVariable String filename) {

        byte[] data = repo.findByName(filename).getData();
        String mimeType = URLConnection.guessContentTypeFromName(filename);
        if (mimeType != null) {
            final HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.valueOf(mimeType));
            return new ResponseEntity<>(data, headers, HttpStatus.OK);
        } else {
            logger.warn("Unable to determine the mimeType for " + filename);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping("/files")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) throws IOException {

        repo.save(new SavedFileEntity(file.getName(), file.getBytes()));
        redirectAttributes.addFlashAttribute("message",
            "You successfully uploaded " + file.getOriginalFilename() + "!");

        return "redirect:/";
    }

}