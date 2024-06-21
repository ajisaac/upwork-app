package co.aisaac.upwork;

import co.aisaac.upwork.model.Posting;
import co.aisaac.upwork.model.PostingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;


@Controller
public class TitlesController {

    @Autowired
    PostingRepo jobRepo;

    TitlesFilter filter = new TitlesFilter();

    @GetMapping("/titles")
    public String all(Model model) {
        List<Posting> jobs = jobRepo.findAllByStatusOrderByPubDateDesc("new");

        if (!filter.titleSearch.isBlank()) {
            jobs = jobs
                    .stream()
                    .filter(job -> job.title.toLowerCase().contains(filter.titleSearch.toLowerCase()))
                    .toList();
        }

        model.addAttribute("jobs", jobs);
        model.addAttribute("filter", filter);

        return "titles";
    }


    @PostMapping("/titles")
    public String searchTitles(TitlesFilter filter) {
        this.filter = filter;
        return "redirect:/titles";
    }
}

