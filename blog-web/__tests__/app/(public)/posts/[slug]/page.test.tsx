import { render, screen } from '@testing-library/react';
import SinglePage from '@/app/(public)/posts/[slug]/page';
import { getPostBySlug } from '@/services/postService';

jest.mock('@/services/firebase', () => ({
    getIdTokenByCustomToken: jest.fn().mockResolvedValue('fake-token'),
}));

jest.mock('@/services/postService');

const mockedGetPostBySlug = getPostBySlug as jest.MockedFunction<typeof getPostBySlug>;

describe('SinglePage', () => {
    it('renders cover image, title, author, and markdown content', async () => {
        const post = {
            postId: '1',
            title: 'Test Post',
            content: '# Heading\nSome content',
            creationDate: '2025-07-31',
            slug: 'test-post',
            coverImage: '/test-image.png',
        };

        mockedGetPostBySlug.mockResolvedValueOnce(post);

        // Mock next/image to render a normal img
        jest.mock('next/image', () => ({
            __esModule: true,
            default: (props: any) => <img {...props} />,
        }));

        // @ts-ignore
        render(await SinglePage({ params: Promise.resolve({ slug: 'test-post' }) }));
        expect(screen.getByAltText('Test Post')).toBeInTheDocument();
        expect(screen.getByText('Test Post')).toBeInTheDocument();
        expect(screen.getByText('Gabriel Inacio')).toBeInTheDocument();
        expect(screen.getByText('2025-07-31')).toBeInTheDocument();
        expect(screen.getByText('Heading')).toBeInTheDocument();
        expect(screen.getByText('Some content')).toBeInTheDocument();
    });

    it('should render error message when post is not found', async () => {
        mockedGetPostBySlug.mockRejectedValueOnce(new Error('Post not found'));

        render(await SinglePage({
            params: Promise.resolve({
                slug: 'non-existent-post'
            })
        }));


        expect(screen.getByText('Post Not Found')).toBeInTheDocument();
    });

    it('should render unknown error message when post is not found', async () => {
        mockedGetPostBySlug.mockRejectedValueOnce("Any error");

        render(await SinglePage({
            params: Promise.resolve({

                slug: 'non-existent-post'
            })
        }));


        expect(screen.getByText("Unknown error")).toBeInTheDocument();
    });
});
