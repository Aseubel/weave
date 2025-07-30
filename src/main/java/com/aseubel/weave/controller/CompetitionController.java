package com.aseubel.weave.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.aseubel.weave.common.ApiResponse;
import com.aseubel.weave.common.annotation.constraint.RequireLogin;
import com.aseubel.weave.common.annotation.constraint.RequirePermission;
import com.aseubel.weave.pojo.dto.competition.CompetitionRequest;
import com.aseubel.weave.pojo.dto.competition.ParticipantRequest;
import com.aseubel.weave.pojo.dto.competition.SubmissionRequest;
import com.aseubel.weave.pojo.dto.competition.SubmissionResponse;
import com.aseubel.weave.pojo.entity.competition.Competition;
import com.aseubel.weave.pojo.entity.competition.Participant;
import com.aseubel.weave.pojo.entity.competition.Submission;
import com.aseubel.weave.service.CompetitionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

/**
 * @author Aseubel
 * @date 2025/7/29 上午9:43
 */
@RestController
@RequestMapping("/api/competition")
@RequiredArgsConstructor
@Tag(name = "比赛控制器")
public class CompetitionController {

    @Resource
    private CompetitionService competitionService;

    /**
     * 创建一个新的比赛
     *
     * @param request 比赛信息
     * @return 创建后的比赛实体
     */
    @PostMapping
    @RequirePermission
    public ApiResponse<Competition> createCompetition(@RequestBody CompetitionRequest request) {
        Competition createdCompetition = competitionService.create(request);
        return ApiResponse.success(createdCompetition);
    }

    /**
     * 开始一个比赛
     *
     * @param competitionId 比赛ID
     * @return 更新后的比赛实体
     */
    @PostMapping("/{competitionId}/start")
    @RequirePermission
    public ApiResponse<Competition> startCompetition(@PathVariable Long competitionId) {
        Competition competition = competitionService.start(competitionId);
        if (ObjectUtil.isNotEmpty(competition)) {
            return ApiResponse.success(competition);
        }
        return ApiResponse.notFound("比赛不存在");
    }

    /**
     * 结束一个比赛
     *
     * @param competitionId 比赛ID
     * @return 更新后的比赛实体
     */
    @PostMapping("/{competitionId}/end")
    @RequirePermission
    public ApiResponse<Competition> endCompetition(@PathVariable Long competitionId) {
        Competition competition = competitionService.end(competitionId);
        if (ObjectUtil.isNotEmpty(competition)) {
            return ApiResponse.success(competition);
        }
        return ApiResponse.notFound("比赛不存在");
    }

    /**
     * 获取比赛详情
     *
     * @param competitionId 比赛ID
     * @return 比赛实体
     */
    @GetMapping("/{competitionId}")
    public ApiResponse<Competition> getCompetition(@PathVariable Long competitionId) {
        Competition competition = competitionService.get(competitionId);
        if (ObjectUtil.isNotEmpty(competition)) {
            return ApiResponse.success(competition);
        }
        return ApiResponse.notFound("比赛不存在");
    }

    /**
     * 获取所有比赛列表
     *
     * @return 比赛列表
     */
    @GetMapping
    public ApiResponse<List<Competition>> getAllCompetitions() {
        List<Competition> competitions = competitionService.getAll();
        if (CollectionUtil.isNotEmpty(competitions)) {
            return ApiResponse.success(competitions);
        }
        return ApiResponse.success(Collections.emptyList());
    }

    /**
     * 给作品投票
     *
     * @param competitionId 比赛ID
     * @param submissionId 作品ID
     * @return 更新后的作品实体
     */
    @PostMapping("/{competitionId}/vote/{submissionId}")
    @RequireLogin
    public ApiResponse<Submission> voteSubmission(@PathVariable Long competitionId, @PathVariable Long submissionId) {
        Submission submission = competitionService.vote(competitionId, submissionId);
        if (ObjectUtil.isNotEmpty(submission)) {
            return ApiResponse.success(submission);
        }
        return ApiResponse.notFound("作品不存在");
    }

    /**
     * 获取比赛的所有作品列表
     *
     * @param competitionId 比赛ID
     * @return 作品列表
     */
    @GetMapping("/{competitionId}/submission")
    public ApiResponse<List<SubmissionResponse>> getSubmissions(@PathVariable Long competitionId) {
        List<Submission> submissions = competitionService.getSubmissions(competitionId);
        if (CollectionUtil.isNotEmpty(submissions)) {
            List<SubmissionResponse> submissionResponses = submissions.stream()
                    .map(SubmissionResponse::fromSubmission)
                    .peek(r -> r.getAuthor().hide())
                    .toList();

            return ApiResponse.success(submissionResponses);
        }
        return ApiResponse.success(Collections.emptyList());
    }

    /**
     * 获取一个作品详情
     *
     * @param submissionId 作品ID
     * @return 作品实体
     */
    @GetMapping("/submission/{submissionId}")
    public ApiResponse<SubmissionResponse> getSubmission( @PathVariable Long submissionId) {
        Submission submission = competitionService.getSubmission(submissionId);
        if (ObjectUtil.isNotEmpty(submission)) {
            SubmissionResponse response = SubmissionResponse.fromSubmission(submission);
            response.getAuthor().hide();
            return ApiResponse.success(response);
        }
        return ApiResponse.notFound("作品不存在");
    }

    /**
     * 获取今天剩余票数
     *
     * @param competitionId 比赛ID
     * @return 剩余票数
     */
    @GetMapping("/{competitionId}/vote/remaining")
    @RequireLogin
    public ApiResponse<Long> getRemainingVotes(@PathVariable Long competitionId) {
        Long remainingVotes = competitionService.getRemainingVotes(competitionId);
        return ApiResponse.success(remainingVotes);
    }

    /**
     * 获取自己的参赛信息
     * @return 参赛人实体
     */
    @GetMapping("/participate")
    @RequireLogin
    public ApiResponse<Participant> getParticipant() {
        return ApiResponse.success(competitionService.getParticipant());
    }

    /**
     * 修改自己的参赛信息
     * @param request 需要修改的信息
     * @return 更新后的参赛人实体
     */
    @PutMapping("/participate")
    @RequireLogin
    public ApiResponse<Participant> modifyParticipant(@RequestBody ParticipantRequest request) {
        Participant updatedParticipant = competitionService.modifyParticipant(request.toEntity());
        return ApiResponse.success(updatedParticipant);
    }

    /**
     * 创建一个新的作品
     *
     * @param request    作品信息
     * @return 创建后的作品实体
     */
    @PostMapping("/submission")
    @RequireLogin
    public ApiResponse<SubmissionResponse> createSubmission(@RequestBody SubmissionRequest request) {
        Submission createdSubmission = competitionService.createSubmission(request);
        SubmissionResponse response = new SubmissionResponse();
        return ApiResponse.success(response.fromSubmission(createdSubmission));
    }
}
