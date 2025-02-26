import React from 'react';
import { FaEdit, FaTrash, FaUndo, FaExternalLinkAlt } from 'react-icons/fa';
import Post from '@/models/post';
import styles from './postTile.module.css';
import Button from '../button/Button';

interface PostTileProps {
    post: Post;
    onEdit?: (id: string) => void;
    onDelete?: (id: string) => void;
    onRestore?: (id: string) => void;
    onOpen?: (id: string) => void;
}

const PostTile: React.FC<PostTileProps> = ({ post, onEdit, onDelete, onRestore, onOpen }) => {
    return (
        <div className={styles.postTile}>
            <div className={styles.postContent}>
                <h3>{post.title}</h3>
                <p>{post.creationDate}</p>
                <p>{post.slug}</p>
            </div>
            <div className={styles.actions}>
                {onEdit && (
                    <Button className={styles.iconButton} onClick={() => { onEdit(post.postId); }} ariaLabel='Edit'>
                        <FaEdit />
                    </Button>
                )}
                {onDelete && (
                    <Button className={styles.iconButton} onClick={() => { onDelete(post.postId); }} ariaLabel='Delete'>
                        <FaTrash />
                    </Button>
                )}
                {onRestore && (
                    <Button className={styles.iconButton} onClick={() => { onRestore(post.postId); }} ariaLabel='Restore'>
                        <FaUndo />
                    </Button>
                )}
                {onOpen && (
                    <Button className={styles.iconButton} onClick={() => { onOpen(post.postId); }} ariaLabel='Open'>
                        <FaExternalLinkAlt />
                    </Button>
                )}
            </div>
        </div>
    );
};

export default PostTile;