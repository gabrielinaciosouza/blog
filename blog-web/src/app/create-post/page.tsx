"use client"

import React, { useState, useEffect, useRef } from "react";
import styles from "./createPostPage.module.css";
import Image from "next/image";
import ReactQuill from "react-quill-new";
import "react-quill-new/dist/quill.snow.css";
import { usePublishPost } from "@/hooks/usePublishPost";
import { FaUpload, FaPlus, FaImage, FaExternalLinkAlt, FaVideo, FaEye, FaCross } from "react-icons/fa";
import BlogImage from "@/models/blog-image";
import PostCard from "@/components/postCard/PostCard";
import Post from "@/models/post";

const Modal: React.FC<{ children: React.ReactNode; onClose: () => void }> = ({ children, onClose }) => {
    return (
        <div className={styles.modalOverlay}>
            <div className={styles.modalContent}>
                {children}
                <button className={styles.closeModalButton} onClick={onClose}>
                    close
                </button>
            </div>
        </div>
    );
};

export default function CreatePostPage() {
    const [open, setOpen] = useState(false);
    const [showPreviewModal, setShowPreviewModal] = useState(false);
    const [postImageFile, setPostImageFile] = useState<File | null>(null);
    const [coverImage, setCoverImage] = useState<BlogImage | null>(null);
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

    const handleImageUpload = async (e: React.ChangeEvent<HTMLInputElement>) => {
        if (e.target.files && e.target.files[0]) {
            setPostImageFile(e.target.files[0]);
            const formData = new FormData();
            formData.append("file", e.target.files[0]);
            formData.append("fileName", e.target.files[0].name);
            formData.append("fileMimeType", e.target.files[0].type);
        
            const result = await fetch(`/api/images?type=cover-images`, {
                method: "POST",
                body: formData,
            });
            const data = await result.json();
            setCoverImage(new BlogImage(data.url, data.fileName));
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

    const post: Post = {
        postId: "",
        title: title,
        content: content,
        coverImage: coverImage ? coverImage.url : "/logo2.png",
        creationDate: new Date().toISOString().split('T')[0],
        slug: "",
    };

    return (
        <div className={styles.container}>
             {showPreviewModal && (
                <Modal onClose={() => setShowPreviewModal(false)}>
                    <PostCard {...post} />
                </Modal>
            )}
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
            </div>
            <div className={styles.editor}>
                <div className={styles.editorButtons}>
                    <button className={styles.button} onClick={() => setOpen(!open)}>
                        <FaPlus />
                    </button>
                    <button className={styles.button} onClick={() => setShowPreviewModal(true)}>
                        <FaEye />
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
        </div>
    );
};