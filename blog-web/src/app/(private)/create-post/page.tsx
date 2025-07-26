"use client"

import React, { useState, useEffect, useRef } from "react";
import { useEditor, EditorContent } from '@tiptap/react';
import StarterKit from '@tiptap/starter-kit';
import Underline from '@tiptap/extension-underline';
import Link from '@tiptap/extension-link';
import Image from '@tiptap/extension-image';
import { useModal } from "@/hooks/useModal";
import { FaUpload, FaPlus, FaImage, FaEye } from "react-icons/fa";
import PostCard from "@/components/postCard/PostCard";
import Post from "@/models/post";
import CreatePostRequest from "@/models/create-post-request";
import { useRouter } from "next/navigation";
import useStorage from "@/hooks/useStorage";
import useLoading from "@/hooks/useLoading";
import Loading from "@/components/loading/Loading";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogDescription, DialogFooter } from "@/components/ui/dialog";

export default function CreatePostPage() {
    React.useEffect(() => {
        const style = document.createElement('style');
        style.innerHTML = `.ProseMirror:focus { outline: none !important; border-color: transparent !important; box-shadow: none !important; }`;
        document.head.appendChild(style);
        return () => {
            document.head.removeChild(style);
        };
    }, []);

    const [title, setTitle] = useStorage("draft-title", "");
    const [content, setContent] = useStorage("draft-content", "");
    const [coverImage, setCoverImage] = useStorage<string | null>("draft-coverImage", null);

    const { modalState, openModal, closeModal } = useModal();
    const { isLoading, startLoading, stopLoading } = useLoading();
    const router = useRouter();

    const titleRef = useRef<HTMLInputElement>(null);
    const contentImageInputRef = useRef<HTMLInputElement>(null);
    const postImageInputRef = useRef<HTMLInputElement>(null);

    const handleImageUpload = async (e: React.ChangeEvent<HTMLInputElement>, type: string) => {
        let message = "Image not uploaded";
        try {
            if (e.target.files && e.target.files[0]) {
                const file = e.target.files[0];

                const formData = new FormData();
                formData.append("file", file);
                formData.append("fileName", file.name);
                formData.append("fileMimeType", file.type);

                const result = await fetch(`/api/images?type=${type}`, {
                    method: "POST",
                    body: formData,
                });
                if (!result.ok) {
                    throw new Error("Failed to upload image");
                }
                const data = await result.json();
                message = "Image uploaded successfully";
                return data.url;
            }
        } catch (error) {
            message = (error as Error).message;
        } finally {
            openModal(message, () => { });
        }
    };

    const handlePublish = async () => {
        if (!title.trim() || !content.trim()) {
            openModal("Title and content are required", () => { });
            return;
        }

        startLoading();

        try {
            const response = await fetch('/api/create-post', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(new CreatePostRequest(title, content, coverImage)),
            }).then(res => res.json());
            openModal("Post saved successfully", () => {
                setTitle("");
                setContent("");
                setCoverImage(null);
                router.push(`/posts/${response.slug}`);
            });
        } catch (error) {
            openModal((error as Error).message, () => { });
        } finally {
            stopLoading();
        }
    };

    const editor = useEditor({
        extensions: [StarterKit, Underline, Link, Image],
        content: content,
        onUpdate: ({ editor }) => {
            setContent(editor.getHTML());
        },
        immediatelyRender: false,
    });

    const insertImageToEditor = (imageUrl: string | undefined) => {
        if (editor && imageUrl) {
            editor.chain().focus().setImage({ src: imageUrl }).run();
        }
    };


    const post: Post = {
        postId: "",
        title: title,
        content: content,
        coverImage: coverImage ?? "/logo2.png",
        creationDate: new Date().toISOString().replace('T', ' ').split('.')[0].slice(0, -3),
        slug: "",
    };

    return (
        <div className="max-w-2xl mx-auto p-6 space-y-6">
            {isLoading && <Loading />}
            <Dialog open={modalState.isOpen} onOpenChange={closeModal}>
                <DialogContent>
                    <DialogHeader>
                        <DialogTitle>Notification</DialogTitle>
                        <DialogDescription>
                            {typeof modalState.content === "string" ? modalState.content : modalState.content}
                        </DialogDescription>
                    </DialogHeader>
                    <DialogFooter>
                        <Button onClick={closeModal}>Close</Button>
                    </DialogFooter>
                </DialogContent>
            </Dialog>
            <Input
                ref={titleRef}
                placeholder="Title"
                value={title}
                onChange={(e) => setTitle(e.target.value)}
                className="mb-4"
            />
            {/* Cover Image Upload Button */}
            <div className="mb-4 flex gap-2">
                <Button variant="outline" onClick={() => postImageInputRef.current?.click()} aria-label="Upload Cover Image">
                    <FaUpload /> Cover Image
                </Button>
                <input
                    type="file"
                    ref={postImageInputRef}
                    accept="image/*"
                    style={{ display: "none" }}
                    onChange={async (e) => {
                        const url = await handleImageUpload(e, "cover-images");
                        if (url) {
                            setCoverImage(url);
                        }
                    }}
                />
                <Button variant="outline" onClick={() => openModal(<PostCard {...post} />, () => { })} aria-label="Preview">
                    <FaEye /> Preview
                </Button>
            </div>
            <div className="border rounded p-2 bg-background mt-4">
                <div className="flex gap-2 mb-2">
                    <Button type="button" size="sm" variant="outline" onClick={() => editor?.chain().focus().toggleBold().run()}><b>Bold</b></Button>
                    <Button type="button" size="sm" variant="outline" onClick={() => editor?.chain().focus().toggleItalic().run()}><i>Italic</i></Button>
                    <Button type="button" size="sm" variant="outline" onClick={() => editor?.chain().focus().toggleUnderline().run()}><u>Underline</u></Button>
                    <Button type="button" size="sm" variant="outline" onClick={() => editor?.chain().focus().setLink({ href: prompt('Enter link URL') || '' }).run()}>Link</Button>
                    <Button type="button" size="sm" variant="outline" onClick={async () => {
                        const input = document.createElement('input');
                        input.type = 'file';
                        input.accept = 'image/*';
                        input.onchange = async (e: any) => {
                            const fileInput = e.target as HTMLInputElement;
                            if (fileInput.files && fileInput.files[0]) {
                                const fakeEvent = { target: fileInput } as React.ChangeEvent<HTMLInputElement>;
                                const url = await handleImageUpload(fakeEvent, "content-images");
                                if (url) {
                                    insertImageToEditor(url);
                                }
                            }
                        };
                        input.click();
                    }} aria-label="Upload Content Image">
                        <FaImage />
                    </Button>
                </div>
                <EditorContent editor={editor} className="min-h-[180px] outline-none focus:outline-none" />
            </div>
            <Button
                onClick={handlePublish}
                disabled={isLoading}
                className="w-full mt-4"
            >
                Publish
            </Button>
        </div>
    );
}