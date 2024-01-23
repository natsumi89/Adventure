package com.example.Adventure.controller;

import com.example.Adventure.domain.Stamps;
import com.example.Adventure.repository.StampRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("")
public class StampController {

    @Autowired
    private StampRepository stampRepository;

    @GetMapping("/to-stamp-card")
    public String toStampCard(Model model, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId != null) {
            // ユーザーIDがセッションに存在する場合、該当ユーザーのスタンプ情報を取得
            List<Stamps> stamps = stampRepository.findStampsByUserId(userId);

            if (!stamps.isEmpty()) {
                // スタンプ情報が存在し、最新のスタンプ情報のstampsがnullでない場合、モデルに追加
                Stamps stamp = stamps.get(stamps.size() - 1);
                if (stamp.getStamps() != null) {
                    model.addAttribute("myStamp", stamp);
                }
            }
        }
        return "stamp";
    }
}


