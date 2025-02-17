"use client"

import React, { useState, useEffect, useRef } from "react";
import styles from "./createPostPage.module.css";
import Image from "next/image";
import ReactQuill from "react-quill-new";
import "react-quill-new/dist/quill.snow.css";
import { usePublishPost } from "@/hooks/usePublishPost";
import { FaUpload, FaPlus, FaImage, FaExternalLinkAlt, FaVideo } from "react-icons/fa";

export default function CreatePostPage() {
    const [open, setOpen] = useState(false);
    const [postImage, setPostImage] = useState<File | null>(null);
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
    const quillRef = useRef<ReactQuill>(null);

    useEffect(() => {
        if (titleRef.current) {
            titleRef.current.style.height = "auto";
            titleRef.current.style.height = `${titleRef.current.scrollHeight}px`;
        }
    }, [title]);

    const handleImageUpload = (e: React.ChangeEvent<HTMLInputElement>) => {
        if (e.target.files && e.target.files[0]) {
            setPostImage(e.target.files[0]);
        }
    };

    const handleImageInsert = () => {
        if (quillRef.current) {
            const range = quillRef.current.getEditor().getSelection();
            const url = prompt("Enter the image URL");
            if (url) {
                if (range) {
                    quillRef.current.getEditor().insertEmbed(range.index, "image", url);
                }
            }
        }
    };

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
            <div className={styles.imageUpload}>
                <input 
                    type="file" 
                    id="postImage" 
                    className={styles.imageInput} 
                    accept="image/*" 
                    onChange={handleImageUpload}
                />
                {postImage && (
                    <div className={styles.imagePreview}>
                        <Image 
                            src={URL.createObjectURL(postImage)} 
                            alt="Post Image" 
                            width={200} 
                            height={200} 
                            className={styles.previewImage}
                        />
                    </div>
                )}
            </div>
            <div className={styles.editor}>
                <div className={styles.editorButtons}>
                    <button className={styles.button} onClick={() => setOpen(!open)}>
                        <FaPlus />
                    </button>
                    <label htmlFor="postImage" className={styles.uploadButton}>
                        <FaUpload />
                    </label>
                </div>
                {open && (
                    <div className={styles.add}>
                        <button className={styles.addButton} onClick={handleImageInsert}>
                            <FaImage />
                        </button>
                        <button className={styles.addButton}>
                            <FaExternalLinkAlt />
                        </button>
                        <button className={styles.addButton}>
                            <FaVideo />
                        </button>
                    </div>
                )}
                <ReactQuill
                    ref={quillRef}
                    className={styles.textArea} 
                    // theme="bubble" 
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