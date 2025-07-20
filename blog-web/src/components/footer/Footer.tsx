import React from "react";
import Link from "next/link";
import { SiFacebook, SiInstagram, SiYoutube, SiTiktok } from "react-icons/si";

const iconProps = {
    size: 18,
    className: "text-muted-foreground"
};

const Footer = () => {
    const currentYear = new Date().getFullYear();
    return (
        <footer className="w-full h-16 px-4 bg-background border-t border-border flex items-center z-40">
            <div className="flex w-full items-center justify-between">
                <span className="text-sm text-muted-foreground">© {currentYear} Gabriel Inacio. All rights reserved.</span>
                <div className="flex gap-4 justify-end">
                    <Link href="/" aria-label="Facebook">
                        <span className="inline-flex items-center justify-center rounded-full bg-muted w-8 h-8 hover:bg-primary transition">
                            <SiFacebook {...iconProps} />
                        </span>
                    </Link>
                    <Link href="/" aria-label="Instagram">
                        <span className="inline-flex items-center justify-center rounded-full bg-muted w-8 h-8 hover:bg-primary transition">
                            <SiInstagram {...iconProps} />
                        </span>
                    </Link>
                    <Link href="/" aria-label="Tiktok">
                        <span className="inline-flex items-center justify-center rounded-full bg-muted w-8 h-8 hover:bg-primary transition">
                            <SiTiktok {...iconProps} />
                        </span>
                    </Link>
                    <Link href="/" aria-label="Youtube">
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