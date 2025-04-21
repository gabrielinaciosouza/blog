export const SERVER_URL = process.env.NEXT_PUBLIC_API_URL || "http://127.0.0.1:8080";
export const COMMENTS_PATH = `${SERVER_URL}/comments`;

export const getComments = async (commentList: string[]) => {
    const commentArray = Object.values(commentList);
    const response = await fetch(`${COMMENTS_PATH}/by-ids/`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(commentArray),
    });

    const data = await response.json();

    if (!response.ok) {
        throw new Error(data.message);
    }

    return data;
}