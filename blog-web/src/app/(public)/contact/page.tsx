"use client";

import { Card, CardContent, CardHeader } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { SiMaildotcom, SiLinkedin, SiGithub, SiYoutube } from "react-icons/si";

const linkedinUrl = process.env.LINKEDIN_URL || "https://linkedin.com";
const githubUrl = process.env.GITHUB_URL || "https://github.com";
const youtubeUrl = process.env.YOUTUBE_URL || "https://youtube.com";
const email = process.env.CONTACT_EMAIL || "your@email.com";

export default function ContactPage() {
    return (
        <div className="flex justify-center items-center min-h-[60vh] bg-background">
            <Card className="max-w-md w-full shadow-lg">
                <CardHeader>
                    <h1 className="text-3xl font-bold text-center mb-2">Let's get in touch!</h1>
                </CardHeader>
                <CardContent>
                    <p className="mb-6 text-muted-foreground text-center">
                        Here you can find my contact. Feel free to reach out through any platform below.
                    </p>
                    <div className="space-y-4">
                        <div className="flex items-center gap-2 w-full">
                            <SiMaildotcom className="h-5 w-5" />
                            <input
                                type="text"
                                value={email}
                                readOnly
                                className="bg-muted px-2 py-1 rounded w-full text-sm focus:outline-none cursor-pointer"
                                onFocus={e => e.target.select()}
                                onClick={e => (e.target as HTMLInputElement).select()}
                            />
                            <Button
                                size="sm"
                                variant="outline"
                                className="ml-2"
                                onClick={() => navigator.clipboard.writeText(email)}
                            >
                                Copy
                            </Button>
                        </div>
                        <Button variant="outline" className="w-full flex justify-start gap-2" asChild>
                            <a href={linkedinUrl} target="_blank" rel="noopener noreferrer">
                                <SiLinkedin className="h-5 w-5" /> LinkedIn
                            </a>
                        </Button>
                        <Button variant="outline" className="w-full flex justify-start gap-2" asChild>
                            <a href={githubUrl} target="_blank" rel="noopener noreferrer">
                                <SiGithub className="h-5 w-5" /> Github
                            </a>
                        </Button>
                        <Button variant="outline" className="w-full flex justify-start gap-2" asChild>
                            <a href={youtubeUrl} target="_blank" rel="noopener noreferrer">
                                <SiYoutube className="h-5 w-5" /> Youtube
                            </a>
                        </Button>
                    </div>
                </CardContent>
            </Card>
        </div>
    );
}
