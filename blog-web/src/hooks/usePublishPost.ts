import { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import { createPost } from "@/services/postService";
import CreatePostRequest from "@/models/create-post-request";
import Post from "@/models/post";

export const usePublishPost = () => {
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const [loading, setLoading] = useState(false);
  const [responseMessage, setResponseMessage] = useState("");
  const [showModal, setShowModal] = useState(false);
  const [postResponse, setPostResponse] = useState<Post | null>(null);
  const router = useRouter();

  useEffect(() => {
    const savedTitle = localStorage.getItem("draft-title");
    const savedContent = localStorage.getItem("draft-content");
    if (savedTitle) setTitle(savedTitle);
    if (savedContent) setContent(savedContent);
  }, []);

  useEffect(() => {
    localStorage.setItem("draft-title", title);
    localStorage.setItem("draft-content", content);
  }, [title, content]);

  const handlePublish = async () => {
    if (!title.trim() || !content.trim()) {
      setResponseMessage("Title and/or content cannot be empty");
      setShowModal(true);
      return;
    }

    setLoading(true);
    setResponseMessage("");

    try {
      const response = await createPost(new CreatePostRequest(title, content));
      setResponseMessage("Post saved successfully!");
      setPostResponse(response);
      setShowModal(true);
    } catch (error) {
      setResponseMessage((error as Error).message);
      setShowModal(true);
    } finally {
      setLoading(false);
    }
  };

  const handleCloseModal = () => {
    setShowModal(false);
    if (postResponse) {
      localStorage.removeItem("draft-title");
      localStorage.removeItem("draft-content");
      router.push(`/posts/${postResponse.slug}`);
    }
  };

  return {
    title,
    setTitle,
    content,
    setContent,
    loading,
    responseMessage,
    showModal,
    handlePublish,
    handleCloseModal,
  };
};