package com.prepai.prepai_backend.service;

import com.prepai.prepai_backend.dto.ResumeParseResponse;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



@Service
public class ResumeParserService {

    //Common tech skills that we need to look for in resume
    private static final List<String> KNOWN_SKILLS = Arrays.asList("java", "spring", "spring boot", "mysql", "postgresql", "mongodb",
            "javascript", "typescript", "react", "next.js", "node.js", "express",
            "python", "django", "flask", "docker", "kubernetes", "aws", "git",
            "html", "css", "tailwind", "rest api", "graphql", "redis", "kafka",
            "microservices", "hibernate", "jpa", "maven", "gradle", "linux");

    public ResumeParseResponse parseFromUrl(String resumeUrl){
        ResumeParseResponse response = new ResumeParseResponse();

        try{
            // Download PDF from Supabase Storage URL
            URL url = new URL(resumeUrl);
            InputStream inputStream = url.openStream();

            //Extract Test using PDFBOX
            PDDocument document = PDDocument.load(inputStream);
            PDFTextStripper stripper = new PDFTextStripper();
            String rawText = stripper.getText(document);
            document.close();

            response.setRawText(rawText);
            response.setSkills(extractSkills(rawText));
            response.setYoe(extractYoe(rawText));
            response.setProjects(extractProjects(rawText));
        } catch (Exception e) {
           //If parsing fails, return empty response, don't crash it
            System.err.println("Resume parsing Failed: " + e.getMessage());
            response.setRawText("");
            response.setSkills(new ArrayList<>());
            response.setYoe("0-1");
            response.setProjects(new ArrayList<>());
        }
        return response;
    }
    // Check which known skills appear in resume text
    private List<String> extractSkills(String Text) {
        List<String> found = new ArrayList<>();
        String lowerText = Text.toLowerCase();

        for(String skill : KNOWN_SKILLS){
            if(lowerText.contains(skill)){
                found.add(skill);
            }

        }
        return found;
    }
    //Simple YOE detection based on keywords
    private String extractYoe(String Text) {
        String lowerText = Text.toLowerCase();

        if(lowerText.contains("fresher") || lowerText.contains("0 year")
                || lowerText.contains("no experience")){
            return "0-1";
        }
        else if(lowerText.contains("1 year") || lowerText.contains("one year")){
            return "1-2";
        }
        else if(lowerText.contains("2 year") || lowerText.contains("two year")){
            return "2-3";
        }
        return "0-1"; //It is default for freshers
    }
    // Extract project names — lines after "Projects" heading
    private List<String> extractProjects(String Text) {
        List<String> projects = new ArrayList<>();
        String[] lines = Text.split("\n");
        boolean inProjectSection = false;

        for(String line : lines){
            String trimmed = line.trim();

            if(trimmed.toLowerCase().contains("project")){
                inProjectSection = true;
                continue;
            }
            //stop at next section heading
            if(inProjectSection && trimmed.toLowerCase().matches(".*(experience|education|skills|certif|achievement).*")){
                break;
            }

            if(inProjectSection && trimmed.length()>5){
                projects.add(trimmed);
            }
        }
        return projects;
    }
}
