package co.aisaac.upwork.main_page;

import co.aisaac.upwork.model.Posting;
import co.aisaac.upwork.model.PostingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
public class WebController {

    @Autowired
    PostingRepo jobRepo;

    MainFilter filter = new MainFilter();

    @GetMapping("/")
    public String index(Model model) {
        List<Posting> jobs = jobRepo.findAllOrderByPostDate();

        jobs.forEach(job -> job.shortDescription = job.description);
        jobs = jobs.stream().filter(job -> {
            return job.description.toUpperCase().contains(filter.searchTerms.toUpperCase())
                    && job.status.equalsIgnoreCase(filter.status);
        }).toList();

        if (Objects.equals(filter.status, "declined")) {
            if (jobs.size() > 100)
                jobs = jobs.subList(0, Math.min(100, jobs.size() - 1));
        }

        // add a filter to this find all method
        model.addAttribute("filter", filter);
        model.addAttribute("jobs", jobs);

        return "index";

    }

    @PostMapping("/")
    public String allFiltered(MainFilter filter) {
        this.filter = filter;
        return "redirect:/";
    }

    @GetMapping("/filter/status/{status}")
    public String filterStatus(@PathVariable("status") String status) {
        filter.setStatus(status);
        return "redirect:/";
    }

    @ResponseBody
    @PostMapping("/status/{id}/{status}")
    public ResponseEntity<String> updateStatus(@PathVariable("id") String id, @PathVariable("status") String status) {
        Long idd = Long.parseLong(id);
        Optional<Posting> optJob = jobRepo.findById(idd);
        if (optJob.isEmpty()) return ResponseEntity.notFound().build();

        Posting job = optJob.get();
        job.setStatus(status);
        jobRepo.save(job);
        return ResponseEntity.ok().build();
    }


//	private void highlight(List<String> searchTerms, List<Job> filtered, String className) {
//		if (searchTerms.isEmpty()) return;
//		if (filtered.isEmpty()) return;
//
//		for (String searchTerm : searchTerms) {
//			String pattern = "(?i)" + Pattern.quote(searchTerm.trim());
//			String st = "<span class=\"" + className + "\">" + searchTerm + "</span>";
//			// for each search term
//			for (Job job : filtered) {
//				job.description = job.description.replaceAll(pattern, st);
//			}
//		}
//	}
}
