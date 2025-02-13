import React from "react";
import styles from "./footer.module.css";
import Image from "next/image";
import Link from "next/link";

const Footer = () => {

    const currentYear = new Date().getFullYear();

    return (
        <div className={styles.container}>
            <div className={styles.topSection}>
                <div className={styles.info}>
                    <div className={styles.logo}>
                        <Image src="/logo2.png" alt="Gabriel's blog" width={48} height={48} />
                    </div>
                    
                    <p className={styles.desc}>
                        Powered by coffee ☕, late-night coding,<br />
                        and an endless love for problem-solving!
                    </p>
                    <p className={styles.year}>Gabriel, {currentYear}</p>
                </div>
                <div className={styles.links}>
                    <div className={styles.list}>
                        <span className={styles.listTitle}>Links</span>
                        <Link href="/" className={styles.listItem}>Homepage</Link>
                        <Link href="/" className={styles.listItem}>Blog</Link>
                        <Link href="/" className={styles.listItem}>About</Link>
                        <Link href="/" className={styles.listItem}>Contact</Link>
                    </div>
                </div>
            </div>
            <div className={styles.divider}></div>
            <div className={styles.footerBottom}>
                <span>© {currentYear} Gabriel Inacio. All rights reserved.</span>
                <div className={styles.socialIcons}>
                    <Link href="/">
                        <Image src="/facebook.png" alt="Facebook" width={18} height={18} />
                    </Link>
                    <Link href="/">
                        <Image src="/instagram.png" alt="Instagram" width={18} height={18} />
                    </Link>
                    <Link href="/">
                        <Image src="/tiktok.png" alt="Tiktok" width={18} height={18} />
                    </Link>
                    <Link href="/">
                        <Image src="/youtube.png" alt="Youtube" width={18} height={18} />
                    </Link>
                </div>
            </div>
        </div>
    );
};

export default Footer;