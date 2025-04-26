import React from "react";
import styles from "./navbar.module.css";
import AnimatedImage from "@/components/animatedImage/AnimatedImage";
import Link from "next/link";
import AuthLinks from "@/components/authLinks/AuthLinks";
import Divider from "@/components/divider/Divider";
import AnimatedText from "@/components/animatedText/AnimatedText";

interface NavbarProps {
    authStatus?: "authenticated" | "notauthenticated";
}

const Navbar: React.FC<NavbarProps> = ({ authStatus = "authenticated" }) => {
    return (
        <>
            <nav className={styles.container} aria-label="Main Navigation" role="navigation">
                <div className={styles.logo}>
                    <AnimatedImage src="/logo2.png" alt="logo2" width={48} height={48} className={styles.image} />
                    <span className={styles.textLogo}><AnimatedText>GABRIEL INACIO</AnimatedText></span>
                </div>
                <ul className={styles.links}>
                    <li><Link href="/" className={styles.link}><AnimatedText>Homepage</AnimatedText></Link></li>
                    <li><Link href="/" className={styles.link}><AnimatedText>Contact</AnimatedText></Link></li>
                    <li><Link href="/" className={styles.link}><AnimatedText>About</AnimatedText></Link></li>
                    <li><AuthLinks authStatus={authStatus} /></li>
                </ul>
            </nav>
            <Divider />
        </>
    );
};

export default Navbar;
