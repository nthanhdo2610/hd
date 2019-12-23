package com.tinhvan.hd.staff.controller;

import com.tinhvan.hd.base.EmptyPayload;
import com.tinhvan.hd.base.HDController;
import com.tinhvan.hd.base.RequestDTO;
import com.tinhvan.hd.staff.model.StaffLogAction;
import com.tinhvan.hd.staff.service.StaffLogActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/staff-log-action")
public class StaffLogActionController extends HDController {
    @Autowired
    StaffLogActionService staffLogActionService;

    /**
     * Created a StaffLogAction
     *
     * @param request object StaffLogAction contain information needed create
     *
     * @return http status code
     */
    @PostMapping(value = "/create")
    public ResponseEntity<?> create(@RequestBody RequestDTO<StaffLogAction> request) {
        StaffLogAction staffLogAction = request.init();
        staffLogActionService.createMQ(staffLogAction);
        return ok("ok");
    }

    /**
     * Get list StaffLogAction
     *
     * @param request EmptyPayload
     *
     * @return http status code
     */
    @PostMapping(value = "/list")
    public ResponseEntity<?> list(@RequestBody RequestDTO<EmptyPayload> request) {
        return ok(staffLogActionService.list());
    }
}
