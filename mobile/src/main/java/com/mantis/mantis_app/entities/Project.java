package com.mantis.mantis_app.entities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Project {
    public String id;
    public String title;
    public String description;
    public List<Requirement> requirements = new ArrayList<>();
    public List<Task> tasks = new ArrayList<>();
    public String userRole;
    public List<ProjectMember> projectMembers = new ArrayList<>();
    public int completionPercentage;
    public int completeTasks = 0;
    public int activeTasks = 0;

    // Singleton
    private static List<Project> allProjects;

    public static void parseProjectData(JSONObject projectData){
        allProjects = new ArrayList<>();
        JSONArray projects;
        try {
            projects = projectData.getJSONArray("projects");
            for (int i = 0; i < projects.length(); i++) {
                JSONObject project = projects.getJSONObject(i).getJSONObject("project");
                String role = projects.getJSONObject(i).getString("role");
                allProjects.add(new Project(project, role));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static List<Project> getAllProjects() { return allProjects; }

    public static Project getProjectByID(String projectID){
        for (Project project: allProjects) if(project.id.equals(projectID)) return project;
        return null;
    }

    public static Task getProjectTaskByID(Project project, String taskID){
        for (Task task: project.tasks) if(task.id.equals(taskID)) return task;
        return null;
    }

    public static void removeProjectWithId(String ID){
        for(int i = 0; i < allProjects.size(); i++)
            if(allProjects.get(i).id.equals(ID)) allProjects.remove(i);
    }

    private Project(JSONObject projectData, String role){
        try {
            title = projectData.getString("title");
            id = projectData.getString("_id");
            description = projectData.getString("description");
            userRole = role;
            requirements = Requirement.parseRequirements(projectData.getJSONArray("requirements"));
            projectMembers = ProjectMember.parseProjectMember(projectData.getJSONArray("users"));
            tasks = Task.parseTasks(projectData.getJSONArray("tasks"));

            // Get number of resolved and un-resolved tasks to calculate % to completion
            for (Task task: tasks) {
                if(task.resolution.equals("Resolved")) completeTasks++;
                else activeTasks++;
            }

            // Truncate decimal places for percentage towards project completion
            if(tasks.size() != 0) completionPercentage = (int)(((double)completeTasks / (double)tasks.size()) * 100);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
