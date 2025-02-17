"use client"

import React, { useState, useEffect, useRef } from "react";
import styles from "./createPostPage.module.css";
import Image from "next/image";
import ReactQuill from "react-quill-new";
import "react-quill-new/dist/quill.bubble.css";
import { usePublishPost } from "@/hooks/usePublishPost";

export default function CreatePostPage() {
    const [open, setOpen] = useState(false);
    const {
        title,
        setTitle,
        content,
        setContent,
        loading,
        responseMessage,
        showModal,
        handlePublish,
        handleCloseModal
    } = usePublishPost();

    const titleRef = useRef<HTMLTextAreaElement>(null);

    useEffect(() => {
        if (titleRef.current) {
            titleRef.current.style.height = "auto";
            titleRef.current.style.height = `${titleRef.current.scrollHeight}px`;
        }
    }, [title]);

    return (
        <div className={styles.container}>
            <textarea 
                ref={titleRef}
                className={styles.input} 
                placeholder="Title" 
                value={title} 
                onChange={(e) => setTitle(e.target.value)}
                rows={1}
            />
            <div className={styles.editor}>
                <button className={styles.button} onClick={() => setOpen(!open)}>
                    <Image src="/plus.png" alt="" width={16} height={16} />
                </button>
                {open && (
                    <div className={styles.add}>
                        <button className={styles.addButton}>
                            <Image src="/image.png" alt="image" width={16} height={16} />
                        </button>
                        <button className={styles.addButton}>
                            <Image src="/external.png" alt="external" width={16} height={16} />
                        </button>
                        <button className={styles.addButton}>
                            <Image src="/video.png" alt="video" width={16} height={16} />
                        </button>
                    </div>
                )}
                <ReactQuill
                    className={styles.textArea} 
                    theme="bubble" 
                    value={content} 
                    onChange={setContent} 
                    placeholder="Write your story..."
                />
            </div>
            <button
                className={styles.publish} 
                onClick={handlePublish} 
                disabled={loading}
            >
                {loading ? "Publishing..." : "Publish"}
            </button>
            {showModal && (
                <div className={styles.modal}>
                    <div className={styles.modalContent}>
                        <p>{responseMessage}</p>
                        <button className={styles.closeButton} onClick={handleCloseModal}>OK</button>
                    </div>
                </div>
            )}
        </div>
    );
};