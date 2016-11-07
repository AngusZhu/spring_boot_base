package com.sinosafe.payment.controller;

import com.sinosafe.payment.common.Constants;
import com.sinosafe.payment.common.SpringContextUtil;
import com.sinosafe.payment.service.common.LogService;
import com.sinosafe.payment.service.common.PrimarykeyService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Angus on Nov 8,2015.
 */
@Controller
@RequestMapping("/")
public class HelloSpringBootController {

    private final static Logger log = LoggerFactory.getLogger(HelloSpringBootController.class);

    @Autowired
    LogService logService;


    @Autowired
    PrimarykeyService primarykeyService;


    @Autowired
    JavaMailSender mailSender;

    @PostMapping("/show")// 这里指定RequestMethod，如果不指定Swagger会把所有RequestMethod都输出，在实际应用中，具体指定请求类型也使接口更为严谨。
    @ApiOperation(value = "测试接口", notes = "测试接口详细描述")
    public String show(
            @ApiParam(required = true, name = "name", value = "姓名")
            @RequestBody String name) {
        System.out.println(name);
        return "success";
    }


    @GetMapping("/")
    public String sayHello(Model model) throws Exception {

        return "forward:index";

//        Long pkId = primarykeyService.getSeqence();
//        logService.saveRequestLog(pkId, "WEIXIN", "sayHello", "1234556", "wahaha");
//        IdWorker idWorker = new IdWorker(0, 0);
//        return "Hello payment! --:";
    }

    @GetMapping("/index")
    public String sayHello2(Model model,HttpServletRequest request) throws Exception {
        Constants constants = (Constants) SpringContextUtil.getBean("constants");

        return "test";

//        Long pkId = primarykeyService.getSeqence();
//        logService.saveRequestLog(pkId, "WEIXIN", "sayHello", "1234556", "wahaha");
//        IdWorker idWorker = new IdWorker(0, 0);
//        return "Hello payment! --:";
    }



    /**
     * just for test http://localhost:9000/sessiontest?action=set&msg=123
     * session 内容保存到了redis 当中
     *
     * @param model
     * @param request
     * @param action
     * @param msg
     * @return
     */
//    @GetMapping("/sessiontest")
//    public String sayHello(Model model, HttpServletRequest request, String action, String msg) {
//        HttpSession session = request.getSession();
//        log.info("action:" + action);
//        log.info("msg:" + msg);
//        log.info(session.getId());
//        String str = "";
//        String jedisContent = "the hell";
//
//        if ("set".equals(action)) {
//            str = msg;
//            session.setAttribute("msg", msg);
//        } else {
//            str = (String) session.getAttribute("msg");
//            model.addAttribute("msgFromRedis", str);
//
//        }
//
//        return "action:" + action + ",msg:" + str + "jeidscontent:" + jedisContent;
//    }

//    @GetMapping("/redis/{name}")
//    public String sayHello(@PathVariable("name") String name) throws UnsupportedEncodingException {
//        RedisConnection connection = redisService.getConnection();
//        StringBuffer sb = new StringBuffer();
//        try {
//            for (int i = 0; i < 100; i++) {
//                connection.setEx(("payment:test:sayHello:name:" + i).getBytes("UTF-8"), 30, name.getBytes("UTF-8"));
//                sb.append(connection.get(("payment:test:sayHello:name:" + i).getBytes("UTF-8")));
//                sb.append("_").append(i).append("|");
//            }
//            return sb.toString();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        } finally {
//            redisService.returnResource(connection);
//        }
//    }

}
