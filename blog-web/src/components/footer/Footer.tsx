import React from "react";
import Link from "next/link";
import { SiLinkedin, SiGithub, SiYoutube } from "react-icons/si";

const iconProps = {
    size: 18,
    className: "text-muted-foreground"
};

const linkedinUrl = process.env.LINKEDIN_URL || "https://linkedin.com";
const githubUrl = process.env.GITHUB_URL || "https://github.com";
const youtubeUrl = process.env.YOUTUBE_URL || "https://youtube.com";

const Footer = () => {
    const currentYear = new Date().getFullYear();
    return (
        <footer className="w-full h-16 px-4 bg-background border-t border-border flex items-center z-40">
            <div className="flex w-full items-center justify-between">
                <span className="text-sm text-muted-foreground">© {currentYear} Gabriel Inacio. All rights reserved.</span>
                <div className="flex gap-4 justify-end">
                    <Link href={linkedinUrl} aria-label="LinkedIn" target="_blank" rel="noopener noreferrer">
                        <span className="inline-flex items-center justify-center rounded-full bg-muted w-8 h-8 hover:bg-primary transition">
                            <SiLinkedin {...iconProps} />
                        </span>
                    </Link>
                    <Link href={githubUrl} aria-label="Github" target="_blank" rel="noopener noreferrer">
                        <span className="inline-flex items-center justify-center rounded-full bg-muted w-8 h-8 hover:bg-primary transition">
                            <SiGithub {...iconProps} />
                        </span>
                    </Link>
                    <Link href={youtubeUrl} aria-label="Youtube" target="_blank" rel="noopener noreferrer">
                        <span className="inline-flex items-center justify-center rounded-full bg-muted w-8 h-8 hover:bg-primary transition">
                            <SiYoutube {...iconProps} />
                        </span>
                    </Link>
                </div>
            </div>
        </footer>
    );
};

export default Footer;