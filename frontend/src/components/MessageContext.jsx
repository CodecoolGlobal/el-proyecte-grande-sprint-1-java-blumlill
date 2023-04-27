import {createContext, useContext, useReducer} from 'react';

const MessageContext = createContext(null);
const MessageDispatchContext = createContext(null);

export function useMessageContext() {
    return useContext(MessageContext);
}

export function useMessageDispatchContext() {
    return useContext(MessageDispatchContext);
}

const empty = "Howdy, Commander! Do something...";

const siliconeImage = '/silicone.png'

export function StationContext({children}) {

    const [message, dispatch] = useReducer(messageReducer, empty, (e) => <div>{e}</div>);

    return (<>
        <MessageContext.Provider value={message}>
            <MessageDispatchContext.Provider value={dispatch}>
                {children}
            </MessageDispatchContext.Provider>
        </MessageContext.Provider>
    </>);
}

const messageReducer = (message, action) => {
    switch (action.type) {
        case 'cost': {
            return <div className="cost">
                <div>Resources needed to add miner ship:</div>
                {Object.keys(action.data).map(key => {
                    console.log(key);
                    console.log(action.data[key]);
                    return <div className="message-row">
                        <img style={{width:"20%", height:"20%"}} src={key.toLowerCase() +'.png'} alt="Silicone" />
                        <p style={{marginLeft:"5px"}} key={key} >{key}: {action.data[key]}</p>
                    </div>
                })}
            </div>;
        }
    }
}


