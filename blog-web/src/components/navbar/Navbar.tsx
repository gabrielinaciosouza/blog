import React from "react";
import styles from "./navbar.module.css";
import Image from "next/image";
import Link from "next/link";
import AuthLinks from "@/components/authLinks/AuthLinks";
import Divider from "@/components/divider/Divider";

const Navbar = () => {
    return (
        <>
            <div className={styles.container}>
                <div className={styles.logo}>
                    <Image src="/logo2.png" alt="logo2" width={48} height={48} className={styles.image}/>
                    <div className={styles.textLogo}>GABRIEL INACIO</div>
                </div>
                <div className={styles.links}>
                    <Link href="/" className={styles.link}>Homepage</Link>
                    <Link href="/" className={styles.link}>Contact</Link>
                    <Link href="/" className={styles.link}>About</Link>
                    <AuthLinks authStatus="authenticated"/>
                </div>
            </div>
            <Divider />
        </>
    )
}

export default Navbar;
