package com.io.game_changer_test.model.issueModel

import com.io.game_changer_test.model.CommentModel.User

data class GithubIssueModel(
    val assignee: Assignee,
    val author_association: String,
    val body: String,
    val closed_at: Any,
    val comments: Int,
    val comments_url: String,
    val created_at: String,
    val events_url: String,
    val html_url: String,
    val id: Int,
    val labels: List<Label>,
    val labels_url: String,
    val locked: Boolean,
    val milestone: Milestone,
    val node_id: String,
    val number: Int,
    val repository_url: String,
    val state: String,
    val title: String,
    val updated_at: String,
    val url: String,
    val user: User
)