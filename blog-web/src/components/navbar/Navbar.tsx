import React from "react";
import styles from "./navbar.module.css";
import AnimatedImage from "@/components/animatedImage/AnimatedImage";
import Link from "next/link";
import Divider from "@/components/divider/Divider";
import AnimatedText from "@/components/animatedText/AnimatedText";

const Navbar: React.FC = () => {
    return (
        <>
            <nav className={styles.container} aria-label="Main Navigation" role="navigation">
                <div className={styles.logo}>
                    <Link href="/">
                        <AnimatedImage src="/logo2.png" alt="logo2" width={48} height={48} className={styles.image} />
                    </Link>
                    <Link href="/" className={styles.textLogo}>
                        <AnimatedText>Gabriel's Blog</AnimatedText>
                    </Link>
                </div>
                <ul className={styles.links}>
                    <li><Link href="/" className={styles.link}><AnimatedText>Homepage</AnimatedText></Link></li>
                    <li><Link href="/" className={styles.link}><AnimatedText>Contact</AnimatedText></Link></li>
                    <li><Link href="/" className={styles.link}><AnimatedText>About</AnimatedText></Link></li>
                </ul>
            </nav>
            <Divider />
        </>
    );
};

export default Navbar;
