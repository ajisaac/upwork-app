package co.aisaac.upwork;

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

//	private List<Job> filterJobs(MainFilter mainFilter, List<Job> all) {
//
//		// filter status
//		String status = mainFilter.getStatus();
//		List<Job> filtered = new ArrayList<>(all);
//		filtered = filtered.stream().filter(job -> job.status.trim().equalsIgnoreCase(status.trim())).toList();
//
//		// filter job site
//		String jobSite = mainFilter.getJobSite();
//		if (!jobSite.equals("ALL")) {
//			filtered = filtered.stream().filter(job -> job.job_site.trim().equalsIgnoreCase(jobSite.trim())).toList();
//		}
//
//		// filter company
//		List<String> companies = Arrays.stream(mainFilter.companySearch.split(",")).filter(s -> !s.isBlank()).toList();
//		if (!companies.isEmpty()) {
//			filtered = filtered.stream().filter(job -> {
//				for (String company : companies)
//					if (job.company.trim().toLowerCase(Locale.ROOT).contains(company.trim().toLowerCase(Locale.ROOT)))
//						return true;
//
//				return false;
//			}).toList();
//		}
//
//		// filter search term
//		List<String> searchTerms = Arrays.stream(mainFilter.searchTerms.split(",")).filter(s -> !s.isBlank()).toList();
//		if (!searchTerms.isEmpty()) {
//			filtered = filtered.stream().filter(job -> {
//				var desc = job.description.toLowerCase();
//				for (String term : searchTerms) {
//					if (desc.contains(term.trim().toLowerCase())) return true;
//					if (job.title.toLowerCase(Locale.ROOT).contains(term.trim().toLowerCase())) return true;
//					if (job.subtitle.toLowerCase(Locale.ROOT).contains(term.trim().toLowerCase())) return true;
//				}
//				return false;
//			}).toList();
//		}
//
//		// filter title
//		List<String> titleSearchTerms = Arrays.stream(mainFilter.titleSearch.split(",")).filter(s -> !s.isBlank()).toList();
//		if (!titleSearchTerms.isEmpty()) {
//			filtered = filtered.stream().filter(job -> {
//				for (String term : titleSearchTerms) {
//					if (job.title.toLowerCase(Locale.ROOT).contains(term.trim().toLowerCase())) return true;
//				}
//				return false;
//			}).toList();
//		}
//
//
//		 highlight search term
//		highlight(searchTerms, filtered, "highlight");
//
//		 sort
//		filtered = filtered.stream().sorted((o1, o2) -> o1.company.compareToIgnoreCase(o2.company)).toList();
//
//		return filtered;
//	}
//
//	private void augmentCompanies(List<Job> jobs) {
//		// add statuses to each company
//
//		Map<String, Set<String>> comps = new HashMap<>();
//
//		for (Job job : jobs) {
//			var comp = job.company;
//			var status = job.status;
//			if (comps.containsKey(comp)) {
//				comps.get(comp).add(status);
//			} else {
//				comps.put(comp, new HashSet<>());
//				comps.get(comp).add(status);
//			}
//		}
//
//		for (Job job : jobs) {
//			var statuses = comps.get(job.company);
//			job.companyStatuses = statuses.stream().toList();
//		}
//	}
//
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
