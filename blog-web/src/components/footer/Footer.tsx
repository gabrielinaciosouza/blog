import React from "react";
import styles from "./footer.module.css";
import Image from "next/image";
import Link from "next/link";
import Divider from "@/components/divider/Divider";

const Footer = () => {
    const currentYear = new Date().getFullYear();
    return (
        <div className={styles.container}>
            <Divider />
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