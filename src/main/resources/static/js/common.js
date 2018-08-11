const $ = (selector)=> {
    return document.querySelector(selector);
}

const $All = (selector => {
    return document.querySelectorAll(selector);
})

const fetchManager = async ({url, method, body, headers, callback}) => {
    const response = await fetch(url, {
        method,
        body,
        headers,
        credentials: "same-origin"
    });
    const data = await response.json();
    callback(response.status, data);
};

const redirect = (url) => {
    return document.location = url;
};

const getElement = (selector, indexMaker, identifier) => {
    return selector(indexMaker(identifier));
};

const idString = (input) => {
    return '#' + input;
};


const next = (element) => {
    return element.nextElementSibling || element.parentElement.firstElementChild;
};

const prev = (element) => {
    return element.previousElementSibling || element.parentElement.lastElementChild;
};

const removeClass = (className, target) => {
    target.classList.remove(className);
};

const addClass = (className, target) => {
    target.classList.add(className);
};

const getIndex = (elem) => (
    Array.prototype.slice.call(elem.parentElement.children).indexOf(elem)
);

const formatMoney = (number) => ( Number(number).toLocaleString() );

