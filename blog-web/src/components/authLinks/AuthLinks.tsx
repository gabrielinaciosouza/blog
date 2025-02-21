"use client"

import React from "react";
import styles from "./authLinks.module.css"
import Link from "next/link";
import {useState} from "react";

const AuthLinks = ({ authStatus }: {authStatus: string}) => {

    const [open, setOpen] = useState(false);
    return (<>
            {authStatus === "notauthenticated" ? (
                <Link href="/login" className={styles.link}>Login</Link>
            ) : (
                <>
                    <Link href="/admin" className={styles.link}>Admin</Link>
                    <span className={styles.link}>Logout</span>
                </>
            )}
            <div className={styles.burger} role="button" onClick={() => setOpen(!open)}>
                <div className={styles.line}></div>
                <div className={styles.line}></div>
                <div className={styles.line}></div>
            </div>
            {open && (
                <div className={styles.responsiveMenu}>
                    <Link href="/">Homepage</Link>
                    <Link href="/">Contact</Link>
                    <Link href="/">About</Link>
                    {authStatus === "notauthenticated" ? (
                        <Link href="/login">Login</Link>
                    ) : (
                        <>
                            <Link href="/admin" >Admin</Link>
                            <span className={styles.link}>Logout</span>
                        </>
                    )}
                </div>
            )}
        </>
    );
};

export default AuthLinks;
