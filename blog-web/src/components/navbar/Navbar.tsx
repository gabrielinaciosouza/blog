import React from "react";
import styles from "./navbar.module.css";
import AnimatedImage from "@/components/animatedImage/AnimatedImage";
import Link from "next/link";
import AuthLinks from "@/components/authLinks/AuthLinks";
import Divider from "@/components/divider/Divider";

interface NavbarProps {
    authStatus?: "authenticated" | "notauthenticated";
}

const Navbar: React.FC<NavbarProps> = ({ authStatus = "authenticated" }) => {
    return (
        <>
            <nav className={styles.container} aria-label="Main Navigation" role="navigation">
                <div className={styles.logo}>
                    <AnimatedImage src="/logo2.png" alt="logo2" width={48} height={48} className={styles.image} />
                    <span className={styles.textLogo}>GABRIEL INACIO</span>
                </div>
                <ul className={styles.links}>
                    <li><Link href="/" className={styles.link}>Homepage</Link></li>
                    <li><Link href="/" className={styles.link}>Contact</Link></li>
                    <li><Link href="/" className={styles.link}>About</Link></li>
                    <li><AuthLinks authStatus={authStatus} /></li>
                </ul>
            </nav>
            <Divider />
        </>
    );
};

export default Navbar;
