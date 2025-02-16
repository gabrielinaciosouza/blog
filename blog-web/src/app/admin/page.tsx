"use client"

import React, { useState } from 'react';
import styles from './admin.module.css';
import { FaEdit, FaTrash, FaUndo } from 'react-icons/fa';
import Post from '@/models/post';

const AdminPage = () => {
    const [posts, setPosts] = useState<Post[]>([
        new Post("1", 'Post 1', 'post-1', '2021-09-01', 'This is the content of post 1'),
        new Post("2", 'Post 2', 'post-2', '2021-09-02', 'This is the content of post 2'),
    ]);

    const [deletedPosts, setDeletedPosts] = useState<Post[]>([
        new Post("3", 'Post 3', 'post-3', '2021-09-03', 'This is the content of post 3'),
        new Post("4", 'Post 4', 'post-4', '2021-09-04', 'This is the content of post 4'),
    ]);

    const handleEdit = (id: string) => {
        // Handle edit logic here
        console.log(`Edit post with id: ${id}`);
    };

    const handleDelete = (id: string) => {
        // Handle delete logic here
        console.log(`Delete post with id: ${id}`);
        const postToDelete = posts.find(post => post.postId === id);
        setPosts(posts.filter(post => post.postId !== id));
        if (postToDelete) {
            setDeletedPosts([...deletedPosts, postToDelete]);
        }
    };

    const handleRestore = (id: string) => {
        // Handle restore logic here
        console.log(`Restore post with id: ${id}`);
        const postToRestore = deletedPosts.find(post => post.postId === id);
        setDeletedPosts(deletedPosts.filter(post => post.postId !== id));
        if (postToRestore) {
            setPosts([...posts, postToRestore]);
        }
    };

    const handleCreateNewPost = () => {
        // Handle create new post logic here
        console.log('Create new post');
    };

    return (
        <div className={styles.adminContainer}>
            <div className={styles.buttonContainer}>
                <button className={styles.createButton} onClick={handleCreateNewPost}>Create New Post</button>
            </div>
            <div className={styles.activePosts}>Active Posts</div>
            <table className={styles.adminTable}>
                <thead>
                    <tr>
                        <th>Title</th>
                        <th className={styles.slugColumn}>Slug</th>
                        <th>Date</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {posts.map(post => (
                        <tr key={post.postId} className={styles.tableRow} onClick={() => handleEdit(post.postId)}>
                            <td>{post.title}</td>
                            <td className={styles.slugColumn}>{post.slug}</td>
                            <td>{post.creationDate}</td>
                            <td className={styles.actionsColumn}>
                                <button className={styles.iconButton} onClick={(e) => { e.stopPropagation(); handleEdit(post.postId); }}>
                                    <FaEdit />
                                </button>
                                <button className={styles.iconButton} onClick={(e) => { e.stopPropagation(); handleDelete(post.postId); }}>
                                    <FaTrash />
                                </button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
            <div className={styles.deletedPostsHeader}>Deleted Posts</div>
            <table className={styles.adminTable}>
                <thead>
                    <tr>
                        <th>Title</th>
                        <th className={styles.slugColumn}>Slug</th>
                        <th>Date</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {deletedPosts.map(post => (
                        <tr key={post.postId} className={styles.tableRow} onClick={() => handleEdit(post.postId)}>
                            <td>{post.title}</td>
                            <td className={styles.slugColumn}>{post.slug}</td>
                            <td>{post.creationDate}</td>
                            <td className={styles.actionsColumn}>
                                <button className={styles.iconButton} onClick={(e) => { e.stopPropagation(); handleEdit(post.postId); }}>
                                    <FaEdit />
                                </button>
                                <button className={styles.iconButton} onClick={(e) => { e.stopPropagation(); handleRestore(post.postId); }}>
                                    <FaUndo />
                                </button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
};

export default AdminPage;