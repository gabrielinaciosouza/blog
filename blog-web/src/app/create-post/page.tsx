"use client"

import React, { useState, useEffect } from "react";
import styles from "./createPostPage.module.css";
import Image from "next/image";
import ReactQuill from "react-quill-new";
import "react-quill-new/dist/quill.bubble.css";
import { usePublishPost } from "@/hooks/usePublishPost";

const CreatePostPage = () => {
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

    return (
        <div className={styles.container}>
            <input 
                className={styles.input} 
                type="text" 
                placeholder="Title" 
                value={title} 
                onChange={(e) => setTitle(e.target.value)}
            />
            <div className={styles.editor}>
                <button className={styles.button} onClick={() => setOpen(!open)}>
                    <Image src="/plus.png" alt="" width={16} height={16} />
                </button>
                {open && (
                    <div className={styles.add}>
                        <button className={styles.addButton}>
                            <Image src="/image.png" alt="" width={16} height={16} />
                        </button>
                        <button className={styles.addButton}>
                            <Image src="/external.png" alt="" width={16} height={16} />
                        </button>
                        <button className={styles.addButton}>
                            <Image src="/video.png" alt="" width={16} height={16} />
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

export default CreatePostPage;
