package hsf302.hsf302project.controller;

import hsf302.hsf302project.entity.OrderEntity;
import hsf302.hsf302project.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
@RequestMapping("/revenue")
public class RevenueController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        double todayRevenue = orderService.getRevenueByDate(LocalDate.now());
        double monthRevenue = orderService.getRevenueByMonth(
                LocalDate.now().getYear(),
                LocalDate.now().getMonthValue()
        );

        long totalOrders = orderService.countOrdersByStatus(OrderEntity.Status.FINISHED);
        long pendingOrders = orderService.countOrdersByStatus(OrderEntity.Status.PENDING);

        model.addAttribute("todayRevenue", todayRevenue);
        model.addAttribute("monthRevenue", monthRevenue);
        model.addAttribute("totalOrders", totalOrders);
        model.addAttribute("pendingOrders", pendingOrders);

        return "revenue/revenueDashboard";
    }

    @GetMapping("/daily")
    public String dailyReport(@RequestParam(required = false) String date, Model model) {
        LocalDate reportDate = date != null ? LocalDate.parse(date) : LocalDate.now();

        double revenue = orderService.getRevenueByDate(reportDate);
        var orders = orderService.getFinishedOrdersByDate(reportDate);

        model.addAttribute("date", reportDate);
        model.addAttribute("revenue", revenue);
        model.addAttribute("orders", orders);

        return "revenue/revenueDaily";
    }

    @GetMapping("/monthly")
    public String monthlyReport(@RequestParam(required = false) Integer year,
                                @RequestParam(required = false) Integer month,
                                Model model) {
        if (year == null) year = LocalDate.now().getYear();
        if (month == null) month = LocalDate.now().getMonthValue();

        double revenue = orderService.getRevenueByMonth(year, month);

        model.addAttribute("year", year);
        model.addAttribute("month", month);
        model.addAttribute("revenue", revenue);

        return "revenue/revenueMonthly";
    }
}