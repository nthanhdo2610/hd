package com.tinhvan.hd.controller;


import com.tinhvan.hd.base.*;
import com.tinhvan.hd.entity.NotificationTemplate;
import com.tinhvan.hd.service.NotificationTemplatService;
import com.tinhvan.hd.vo.NotificationTemplateCreate;
import com.tinhvan.hd.vo.NotificationTemplateList;
import com.tinhvan.hd.vo.NotificationTemplateUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author tuongnk on 6/28/2019
 * @project notification
 * @controller Notification template
 */

@RestController
@RequestMapping("/api/v1/notification/template")
public class NotificationTemplateController extends HDController {

    @Autowired
    NotificationTemplatService notificationTemplatService;

    /**
     * Find list of notification template
     *
     * @param res NotificationTemplateList contain info need filter
     * @return list notification template
     */
    @PostMapping("/list")
    public ResponseEntity<?> getList(@RequestBody RequestDTO<NotificationTemplateList> res) {
        NotificationTemplateList notificationTemplateList = res.init();
        return ok(notificationTemplatService.getList(notificationTemplateList));
    }

    /**
     * Create a bew notification template
     *
     * @param request NotificationTemplateCreate contain info of new notification template
     * @return NotificationTemplate contain info after create successfully
     */
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody RequestDTO<NotificationTemplateCreate> request) {
        NotificationTemplateCreate notificationTemplateCreate = request.init();
        NotificationTemplate notificationTemplate = new NotificationTemplate();
        notificationTemplate.setNotifiTemplateCreate(notificationTemplateCreate);
        notificationTemplate.setLangcode(request.langCode());
        notificationTemplate.setCreatedAt(new Date());
        notificationTemplate.setCreatedBy(request.jwt().getUuid());
        return ok(notificationTemplatService.createOrUpdate(notificationTemplate));
    }

    /**
     * Update a bew notification template
     *
     * @param req NotificationTemplateUpdate contain info of notification template need to update
     * @return NotificationTemplate contain info after update successfully
     */
    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestBody RequestDTO<NotificationTemplateUpdate> req) {

        NotificationTemplateUpdate notificationTemplateUpdate = req.init();
        int notifyId = notificationTemplateUpdate.getId();
        NotificationTemplate notificationTemplate = notificationTemplatService.getById(notifyId);
        if (notificationTemplate != null) {
            notificationTemplate.setNotifiTemplateUpdate(notificationTemplateUpdate);
            notificationTemplate.setLangcode(req.langCode());
            notificationTemplate.setModifiedBy(req.jwt().getUuid());
            notificationTemplate.setModifiedAt(req.now());
            return ok(notificationTemplatService.createOrUpdate(notificationTemplate));
        }
        return unauthorized(1220, "Notification is not exits");

    }

    /**
     * Delete a notification template
     *
     * @param req contain id of notification template
     * @return
     */
    @PostMapping("/delete")
    public ResponseEntity<?> deleteNotificationTemplateById(@RequestBody RequestDTO<IdPayload> req) {

        IdPayload payload = req.init();
        Integer templateId = (Integer) payload.getId();

        NotificationTemplate notificationTemplate = notificationTemplatService.getById(templateId);
        if (notificationTemplate == null) {
            throw new BadRequestException(404, "NotificationTemplate is not exits");
        }

        try {
            notificationTemplate.setIsDeleted(1);
            notificationTemplate.setDeletedAt(new Date());
            notificationTemplatService.createOrUpdate(notificationTemplate);
        } catch (Exception ex) {
            throw new InternalServerErrorException();
        }
        return ok(null);
    }

    /**
     * View detail of one notification template
     *
     * @param req contain id of notification template
     * @return
     */
    @PostMapping("/detail")
    public ResponseEntity<?> getNotificationTemplateById(@RequestBody RequestDTO<IdPayload> req) {
        NotificationTemplate template = null;
        try {
            IdPayload payload = req.init();
            Integer templateId = (Integer) payload.getId();
            template = notificationTemplatService.getById(templateId);
            if (template == null) {
                throw new BadRequestException(404, "NotificationTemplate is not exits");
            }
        } catch (Exception ex) {
            throw new InternalServerErrorException();
        }
        return ok(template);
    }

//    @PostMapping("/num")
//    public ResponseEntity<?> getAllNotificationGroupModifiedBy(@RequestBody RequestDTO<NotificationTemplateFilterVO> req) {
//        long total = 0;
//        try {
//            NotificationTemplateFilterVO vo = req.getPayload();
//            total = notificationTemplatService.countNotificationTemplateModifiedBy(vo);
//        } catch (Exception ex) {
//            throw new InternalServerErrorException();
//        }
//        return ok(total);
//    }
}
